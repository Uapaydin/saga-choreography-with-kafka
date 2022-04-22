package com.utku.saga.aspect;

import com.utku.saga.model.RemoteCallRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * @author tcuapaydin
 * @created 19/04/2022 - 18:44
 */

@Aspect
@Configuration
@Slf4j
public class SagaTransactionCollector {

    @Autowired
    private SagaTransactionHandler sagaTransactionHandler;

    @Before(value = "@within(Saga) && args(remoteCallRequest,..)")
    public void beforeAdvice(JoinPoint joinPoint, RemoteCallRequest remoteCallRequest) {
        log.warn("Before method:" + joinPoint.getSignature());
        log.warn("called remoteUrl " + remoteCallRequest);
    }

    @After(value = "@within(Saga) && args(remoteCallRequest,..)")
    public void afterAdvice(JoinPoint joinPoint, RemoteCallRequest remoteCallRequest) {
        sagaTransactionHandler.getTransactionHistory().add(remoteCallRequest);
        log.warn("After method:" + joinPoint.getSignature());
        log.warn("call completed remoteUrl " + remoteCallRequest);
    }

}
