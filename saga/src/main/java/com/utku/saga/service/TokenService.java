package com.utku.saga.service;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.utku.saga.enumaration.AuthorizationType;
import com.utku.saga.model.RemoteCallPackage;
import com.utku.saga.model.RemoteCallRequest;
import com.utku.saga.model.RemoteRestCallPackage;
import com.utku.saga.prop.JwtValidationProperties;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.encoder.org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.rmi.Remote;
import java.util.Base64;
import java.util.HashMap;

/**
 * @author tcuapaydin
 * @created 19/04/2022 - 18:08
 */
@Service
@Order(Integer.MIN_VALUE)
@Slf4j
public class TokenService implements InitializingBean {

    private static TokenService instance;
    private final ObjectMapper objectmapper;
    private final RemoteCall remoteCall;

    @Override
    public void afterPropertiesSet() {
        instance = this;
    }
    public static TokenService get(){
        return instance;
    }

    private final JwtValidationProperties jwtValidationProperties;
    private String serviceJwtToken = null;
    public TokenService(JwtValidationProperties jwtValidationProperties){
        this.jwtValidationProperties = jwtValidationProperties;
        this.objectmapper = new ObjectMapper();
        this.remoteCall = new RemoteCall();
        objectmapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectmapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
    }

    public String getServiceJwtToken(){
        if(StringUtils.isEmpty(serviceJwtToken)){
            if(StringUtils.isEmpty(jwtValidationProperties.getJwksUrl() )){
                log.warn("Service token generation requires jwt.validation.jwks-url property definition");
            }else{
                if(StringUtils.isEmpty(jwtValidationProperties.getTokenKey())){
                    log.warn("Service token generation requires jwt.validation.token-key property definition");
                }else{
                    generateServiceToken();
                }
            }
        }
        return serviceJwtToken;
    }

    private void generateServiceToken(){
        if(Boolean.TRUE.equals(jwtValidationProperties.getGenerateServiceToken())){
            RemoteCallRequest remoteCallRequest = new RemoteCallRequest();
            RemoteRestCallPackage processRequest = new RemoteRestCallPackage();
            processRequest.setAuthorizationType(AuthorizationType.NONE);
            processRequest.setHttpMethod(HttpMethod.POST);
            processRequest.setUrl(jwtValidationProperties.getJwksUrl() + "/api/v1/generate/service-token");
            remoteCallRequest.setProcessRequest(processRequest);
            serviceJwtToken = remoteCall.callForObject(remoteCallRequest, String.class);
        }else{
            log.info("Service token generation is disabled");
        }
    }


}
