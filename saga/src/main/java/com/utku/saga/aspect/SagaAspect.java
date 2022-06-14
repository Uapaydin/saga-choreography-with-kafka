package com.utku.saga.aspect;

import com.utku.saga.model.RemoteCallRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author tcuapaydin
 * @created 19/04/2022 - 18:44
 */

@Aspect
@Component
@Slf4j
public class SagaAspect {

    private final SagaTransactionHandler sagaTransactionHandler;

    public SagaAspect(SagaTransactionHandler sagaTransactionHandler) {
        this.sagaTransactionHandler = sagaTransactionHandler;
    }

    @Before(value = "@within(Saga) && args(remoteCallRequest,..)")
    public void beforeAdvice(JoinPoint joinPoint, RemoteCallRequest remoteCallRequest) {
        log.warn("Before method:" + joinPoint.getSignature());
        log.warn("called remoteUrl " + remoteCallRequest);
    }

    @AfterReturning(value = "@within(Saga) && args(remoteCallRequest,..)")
    public void afterAdvice(JoinPoint joinPoint, RemoteCallRequest remoteCallRequest) {
        sagaTransactionHandler.getTransactionHistory().add(remoteCallRequest);
        log.warn("After method:" + joinPoint.getSignature());
        log.warn("call completed remoteUrl " + remoteCallRequest);
    }

}
