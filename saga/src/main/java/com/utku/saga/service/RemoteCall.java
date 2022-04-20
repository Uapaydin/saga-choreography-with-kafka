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

    private TokenService tokenService;
    private RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public RemoteCall() {
        restTemplate = new RestTemplate();
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        tokenService = TokenService.get();
    }

    public <T> T callForObject(String url, HttpMethod httpMethod, Class<T> type, AuthorizationType authorizationType, Object body) {
        RemoteCallDTO<T> remoteCallDTO = makeCall(url, httpMethod, authorizationType, body);

        return objectMapper.convertValue(remoteCallDTO.getContent(), type);
    }

    public <T> List<T> callForList(String url, HttpMethod httpMethod, Class<T> type, AuthorizationType authorizationType, Object body) {
        RemoteCallDTO<T> remoteCallDTO = makeCall(url, httpMethod, authorizationType, body);

        JavaType listType = objectMapper.getTypeFactory().constructCollectionType(List.class, type);

        return objectMapper.convertValue(remoteCallDTO.getContent(), listType);
    }

    public <K, V> Map<K, V> callForMap(String url, HttpMethod httpMethod, Class<K> typeKey, Class<V> typeValue, AuthorizationType authorizationType, Object body) {
        RemoteCallDTO<Map<K, V>> remoteCallDTO = makeCall(url, httpMethod, authorizationType, body);

        JavaType mapType = objectMapper.getTypeFactory().constructMapType(Map.class, typeKey, typeValue);

        return objectMapper.convertValue(remoteCallDTO.getContent(), mapType);
    }

    public <T> T callForObject(String url, HttpMethod httpMethod, Class<T> type, AuthorizationType authorizationType) {
        RemoteCallDTO<T> remoteCallDTO = makeCall(url, httpMethod, authorizationType, null);

        return objectMapper.convertValue(remoteCallDTO.getContent(), type);
    }

    public <T> List<T> callForList(String url, HttpMethod httpMethod, Class<T> type, AuthorizationType authorizationType) {
        RemoteCallDTO<T> remoteCallDTO = makeCall(url, httpMethod, authorizationType, null);

        JavaType listType = objectMapper.getTypeFactory().constructCollectionType(List.class, type);

        return objectMapper.convertValue(remoteCallDTO.getContent(), listType);
    }

    public <K, V> Map<K, V> callForMap(String url, HttpMethod httpMethod, Class<K> typeKey, Class<V> typeValue, AuthorizationType authorizationType) {
        RemoteCallDTO<Map<K, V>> remoteCallDTO = makeCall(url, httpMethod, authorizationType, null);
        JavaType mapType = objectMapper.getTypeFactory().constructMapType(Map.class, typeKey, typeValue);

        return objectMapper.convertValue(remoteCallDTO.getContent(), mapType);
    }

    @SafeVarargs
    @SuppressWarnings("rawtypes")
    private RemoteCallDTO makeCall(String url, HttpMethod httpMethod, AuthorizationType authorizationType,
                                   @Nullable Object body, MultiValueMap<String, String>... headers) {
        log.info("Make {} request to url: {}", httpMethod.name(), url);
        ResponseEntity<?> responseEntity;
        RemoteCallDTO remoteCallDTO = new RemoteCallDTO();
        HttpEntity<Object> entity = createHeader(authorizationType, body, headers);

        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        responseEntity = restTemplate.exchange(url, httpMethod, entity, Object.class);
        stopWatch.stop();

        try {
            ObjectMapper mapper = new ObjectMapper();
            remoteCallDTO = Optional.ofNullable(mapper.convertValue(responseEntity.getBody(), RemoteCallDTO.class))
                    .orElseThrow(() -> new RemoteCallException("Error occurred during service call to: " + url));
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
                    httpMethod, url, authorizationType, stopWatch.getTotalTimeMillis(), remoteCallDTO);
        } else {
            log.info("{} request to url: {} | Authorization type: {} | completed in {} ms",
                    httpMethod, url, authorizationType, stopWatch.getTotalTimeMillis());
        }

        if (remoteCallDTO.getReturnCode() != ReturnType.SUCCESS.getCode()) {
            throw new RemoteCallException(remoteCallDTO.getError());
        }

        return remoteCallDTO;
    }

    @SafeVarargs
    private HttpEntity<Object> createHeader(AuthorizationType authorizationType,
                                            Object body, MultiValueMap<String, String>... headerList) {
        HttpHeaders headers = new HttpHeaders();

        if (!CollectionUtils.isEmpty(Arrays.asList(headerList))) {
            headers.addAll(Arrays.asList(headerList).get(0));
        }
        String requestId = MDC.get(LogKey.REQUEST_ID.getValue())!=null? MDC.get(LogKey.REQUEST_ID.getValue()).toString():null;
        headers.set(LogKey.REQUEST_ID.getValue(), requestId);
        headers.set(HttpHeaders.ACCEPT_LANGUAGE, LocaleContextHolder.getLocale().getLanguage());

        if (authorizationType == AuthorizationType.USER) {
//            User user = UserService.getUser(true);
//            headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + user.getToken());
        } else if (authorizationType == AuthorizationType.SYSTEM) {
            String serviceJwt = tokenService.getServiceJwtToken();
            if (StringUtils.isEmpty(serviceJwt)) {
                log.error("While using AuthorizationType.SYSTEM, you can not use empty JWT");
                throw new AuthorizationException();
            }
            headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + serviceJwt);
        } else {
            log.info("AuthorizationType: {} | Request Header will be empty", authorizationType);
        }

        return new HttpEntity<>(body, headers);
    }


}
