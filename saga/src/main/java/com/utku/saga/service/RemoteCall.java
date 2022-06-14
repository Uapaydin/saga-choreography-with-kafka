package com.utku.saga.service;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.utku.saga.aspect.Saga;
import com.utku.saga.dto.RemoteCallDTO;
import com.utku.saga.enumaration.AuthorizationType;
import com.utku.saga.enumaration.LogKey;
import com.utku.saga.enumaration.ReturnType;
import com.utku.saga.exception.AuthorizationException;
import com.utku.saga.exception.RemoteCallException;
import com.utku.saga.exception.SagaTransactionException;
import com.utku.saga.model.RemoteCallRequest;
import com.utku.saga.model.RemoteRestCallPackage;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.encoder.org.apache.commons.lang3.StringUtils;
import org.jboss.logging.MDC;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author tcuapaydin
 * @created 19/04/2022 - 18:08
 */
@Slf4j
@Saga
@Service
public class RemoteCall  {
    private static final String REQUEST_TYPE_TRANSACTION = "TRANSACTION";
    private static final String REQUEST_TYPE_COMPENSATION = "COMPENSATION";
    private static final String BEARER = "Bearer ";

    private RestTemplate restTemplate;
    private ObjectMapper objectMapper;

    public RemoteCall (){
        restTemplate = new RestTemplate();
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public <T> T callForCompensation(RemoteCallRequest remoteCallRequest, Class<T> type) {
        RemoteCallDTO<T> remoteCallDTO = makeCall(remoteCallRequest,REQUEST_TYPE_COMPENSATION);
        return objectMapper.convertValue(remoteCallDTO.getContent(), type);
    }
    public <T> T callForObject(RemoteCallRequest remoteCallRequest, Class<T> type) {
        RemoteCallDTO<T> remoteCallDTO = makeCall(remoteCallRequest,REQUEST_TYPE_TRANSACTION);
        return objectMapper.convertValue(remoteCallDTO.getContent(), type);
    }
    public <T> List<T> callForList(RemoteCallRequest remoteCallRequest, Class<T> type) {
        RemoteCallDTO<T> remoteCallDTO = makeCall(remoteCallRequest,REQUEST_TYPE_TRANSACTION);
        JavaType listType = objectMapper.getTypeFactory().constructCollectionType(List.class, type);
        return objectMapper.convertValue(remoteCallDTO.getContent(), listType);
    }

    public <K, V> Map<K, V> callForMap(RemoteCallRequest remoteCallRequest,Class<K> typeKey, Class<V> typeValue) {
        RemoteCallDTO<Map<K, V>> remoteCallDTO = makeCall(remoteCallRequest,REQUEST_TYPE_TRANSACTION);
        JavaType mapType = objectMapper.getTypeFactory().constructMapType(Map.class, typeKey, typeValue);

        return objectMapper.convertValue(remoteCallDTO.getContent(), mapType);
    }

    private RemoteCallDTO makeCall(RemoteCallRequest remoteCallRequest, String requestType) {
        RemoteRestCallPackage remoteRestCallPackage;
        if(REQUEST_TYPE_COMPENSATION.equals(requestType)){
            remoteRestCallPackage = (RemoteRestCallPackage) remoteCallRequest.getCompensationRequest();
        }else{
            remoteRestCallPackage = remoteCallRequest.getProcessRequest();
        }

        log.info("Make {} request to url: {}", remoteRestCallPackage.getHttpMethod().name(), remoteRestCallPackage.getUrl());
        ResponseEntity<?> responseEntity;
        RemoteCallDTO remoteCallDTO = new RemoteCallDTO();
        HttpEntity<Object> entity = createHeader(remoteRestCallPackage);

        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try{
            responseEntity = restTemplate.exchange(remoteRestCallPackage.getUrl(),
                    remoteRestCallPackage.getHttpMethod(), entity, Object.class);
        }catch (Exception e){
            throw new SagaTransactionException(HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage());
        }
        stopWatch.stop();

        try {
            ObjectMapper mapper = new ObjectMapper();
            remoteCallDTO = Optional.ofNullable(mapper.convertValue(responseEntity.getBody(), RemoteCallDTO.class))
                    .orElseThrow(() -> new RemoteCallException("Error occurred during service call to: " +
                            remoteRestCallPackage.getUrl()));
        } catch (ClassCastException | IllegalArgumentException castException) {
            if (responseEntity.getStatusCodeValue() == HttpStatus.OK.value()) {
                remoteCallDTO.setReturnCode(ReturnType.SUCCESS.getCode());
                remoteCallDTO.setReturnMessage(ReturnType.SUCCESS.getMessage());
            } else {
                remoteCallDTO.setReturnCode(ReturnType.FAILURE.getCode());
                remoteCallDTO.setReturnMessage(ReturnType.FAILURE.getMessage());
                remoteCallDTO.setError(ReturnType.FAILURE.getMessage());
            }
            remoteCallDTO.setContent(responseEntity.getBody());
        }

        if (log.isDebugEnabled()) {
            log.debug("{} request to url: {} | Authorization type: {} | completed in {} ms | response: {}",
                    remoteRestCallPackage.getHttpMethod(),
                    remoteRestCallPackage.getUrl(),
                    remoteRestCallPackage.getAuthorizationType(),
                    stopWatch.getTotalTimeMillis(), remoteCallDTO);
        } else {
            log.info("{} request to url: {} | Authorization type: {} | completed in {} ms",
                    remoteRestCallPackage.getHttpMethod(),
                    remoteRestCallPackage.getUrl(),
                    remoteRestCallPackage.getAuthorizationType(),
                    stopWatch.getTotalTimeMillis());
        }

        if (remoteCallDTO.getReturnCode() != ReturnType.SUCCESS.getCode()) {
            throw new RemoteCallException(remoteCallDTO.getError());
        }

        return remoteCallDTO;
    }

    private HttpEntity<Object> createHeader(RemoteRestCallPackage  remoteRestCallPackage){
        HttpHeaders headers = new HttpHeaders();

        if (remoteRestCallPackage.getHeaders() != null && !CollectionUtils.isEmpty(List.of(remoteRestCallPackage.getHeaders()))) {
            headers.addAll(List.of(remoteRestCallPackage.getHeaders()).get(0));
        }
        String requestId = MDC.get(LogKey.REQUEST_ID.getValue())!=null? MDC.get(LogKey.REQUEST_ID.getValue()).toString():null;
        headers.set(LogKey.REQUEST_ID.getValue(), requestId);
        headers.set(HttpHeaders.ACCEPT_LANGUAGE, LocaleContextHolder.getLocale().getLanguage());
        if(StringUtils.isNotEmpty(remoteRestCallPackage.getJwt())){
            headers.set(HttpHeaders.AUTHORIZATION, BEARER + remoteRestCallPackage.getJwt());
        }else if (remoteRestCallPackage.getAuthorizationType() == AuthorizationType.USER) {
            //Passing user token should be handled here
        } else if (remoteRestCallPackage.getAuthorizationType() == AuthorizationType.SYSTEM) {
            //Creating and passing service token should be handled here
        } else {
            log.info("AuthorizationType: {} | Request Header will be empty", remoteRestCallPackage.getAuthorizationType());
        }

        return new HttpEntity<>(remoteRestCallPackage.getBody(), headers);
    }

}
