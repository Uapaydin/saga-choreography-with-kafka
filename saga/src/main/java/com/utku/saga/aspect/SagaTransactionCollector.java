package com.utku.saga.aspect;

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

    @Before(value = "@within(Saga) && args(url,..)")
    public void beforeAdvice(JoinPoint joinPoint, String url) {
        log.warn("Before method:" + joinPoint.getSignature());
        log.warn("called remoteUrl " + url);
    }

    @After(value = "@within(Saga) && args(url,..)")
    public void afterAdvice(JoinPoint joinPoint, String url) {
        sagaTransactionHandler.getTransactionHistory().add(url);
        log.warn("After method:" + joinPoint.getSignature());
        log.warn("call completed remoteUrl " + url);
    }

}
