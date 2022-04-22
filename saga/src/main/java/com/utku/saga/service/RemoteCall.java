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
import com.utku.saga.model.RemoteCallRequest;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.encoder.org.apache.commons.lang3.StringUtils;
import org.jboss.logging.MDC;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.*;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author tcuapaydin
 * @created 19/04/2022 - 18:08
 */
@Slf4j
@Service
@Saga
public class RemoteCall  {

    private final TokenService tokenService;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public RemoteCall() {
        restTemplate = new RestTemplate();
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        tokenService = TokenService.get();
    }

    public <T> T callForObject(RemoteCallRequest remoteCallRequest, Class<T> type) {
        RemoteCallDTO<T> remoteCallDTO = makeCall(remoteCallRequest);
        return objectMapper.convertValue(remoteCallDTO.getContent(), type);
    }

    public <T> List<T> callForList(RemoteCallRequest remoteCallRequest, Class<T> type) {
        RemoteCallDTO<T> remoteCallDTO = makeCall(remoteCallRequest);
        JavaType listType = objectMapper.getTypeFactory().constructCollectionType(List.class, type);
        return objectMapper.convertValue(remoteCallDTO.getContent(), listType);
    }

    public <K, V> Map<K, V> callForMap(RemoteCallRequest remoteCallRequest,Class<K> typeKey, Class<V> typeValue) {
        RemoteCallDTO<Map<K, V>> remoteCallDTO = makeCall(remoteCallRequest);
        JavaType mapType = objectMapper.getTypeFactory().constructMapType(Map.class, typeKey, typeValue);

        return objectMapper.convertValue(remoteCallDTO.getContent(), mapType);
    }

    @SuppressWarnings("rawtypes")
    private RemoteCallDTO makeCall(RemoteCallRequest remoteCallRequest) {
        log.info("Make {} request to url: {}", remoteCallRequest.getProcessRequest().getHttpMethod().name(),
                remoteCallRequest.getProcessRequest().getUrl());
        ResponseEntity<?> responseEntity;
        RemoteCallDTO remoteCallDTO = new RemoteCallDTO();
        HttpEntity<Object> entity = createHeader(remoteCallRequest);

        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        responseEntity = restTemplate.exchange(remoteCallRequest.getProcessRequest().getUrl(),
                remoteCallRequest.getProcessRequest().getHttpMethod(), entity, Object.class);
        stopWatch.stop();

        try {
            ObjectMapper mapper = new ObjectMapper();
            remoteCallDTO = Optional.ofNullable(mapper.convertValue(responseEntity.getBody(), RemoteCallDTO.class))
                    .orElseThrow(() -> new RemoteCallException("Error occurred during service call to: " +
                            remoteCallRequest.getProcessRequest().getUrl()));
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
                    remoteCallRequest.getProcessRequest().getHttpMethod(),
                    remoteCallRequest.getProcessRequest().getUrl(),
                    remoteCallRequest.getProcessRequest().getAuthorizationType(),
                    stopWatch.getTotalTimeMillis(), remoteCallDTO);
        } else {
            log.info("{} request to url: {} | Authorization type: {} | completed in {} ms",
                    remoteCallRequest.getProcessRequest().getHttpMethod(),
                    remoteCallRequest.getProcessRequest().getUrl(),
                    remoteCallRequest.getProcessRequest().getAuthorizationType(),
                    stopWatch.getTotalTimeMillis());
        }

        if (remoteCallDTO.getReturnCode() != ReturnType.SUCCESS.getCode()) {
            throw new RemoteCallException(remoteCallDTO.getError());
        }

        return remoteCallDTO;
    }

    private HttpEntity<Object> createHeader(RemoteCallRequest remoteCallRequest){
        HttpHeaders headers = new HttpHeaders();

        if (remoteCallRequest.getProcessRequest().getHeaders() != null && !CollectionUtils.isEmpty(List.of(remoteCallRequest.getProcessRequest().getHeaders()))) {
            headers.addAll(List.of(remoteCallRequest.getProcessRequest().getHeaders()).get(0));
        }
        String requestId = MDC.get(LogKey.REQUEST_ID.getValue())!=null? MDC.get(LogKey.REQUEST_ID.getValue()).toString():null;
        headers.set(LogKey.REQUEST_ID.getValue(), requestId);
        headers.set(HttpHeaders.ACCEPT_LANGUAGE, LocaleContextHolder.getLocale().getLanguage());

        if (remoteCallRequest.getProcessRequest().getAuthorizationType() == AuthorizationType.USER) {
//            User user = UserService.getUser(true);
//            headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + user.getToken());
        } else if (remoteCallRequest.getProcessRequest().getAuthorizationType() == AuthorizationType.SYSTEM) {
            String serviceJwt = tokenService.getServiceJwtToken();
            if (StringUtils.isEmpty(serviceJwt)) {
                log.error("While using AuthorizationType.SYSTEM, you can not use empty JWT");
                throw new AuthorizationException();
            }
            headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + serviceJwt);
        } else {
            log.info("AuthorizationType: {} | Request Header will be empty", remoteCallRequest.getProcessRequest().getAuthorizationType());
        }

        return new HttpEntity<>(remoteCallRequest.getProcessRequest().getBody(), headers);
    }


}
