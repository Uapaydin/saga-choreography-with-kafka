package com.utku.saga.handler;

import com.utku.saga.aspect.SagaTransactionHandler;
import com.utku.saga.builder.ResponseBuilder;
import com.utku.saga.enumaration.ReturnType;
import com.utku.saga.exception.SagaTransactionException;
import com.utku.saga.model.RemoteCallRequest;
import com.utku.saga.model.RemoteKafkaCallPackage;
import com.utku.saga.service.RemoteCall;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;


/**
 * @author tcuapaydin
 * @created 13/06/2022 - 14:29
 */
@RestControllerAdvice
@ConditionalOnProperty(value = "config.saga.active", havingValue = "true")
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class SagaExceptionHandler {

    private final SagaTransactionHandler sagaTransactionHandler;
    private final RemoteCall remoteCall;

    @ExceptionHandler(SagaTransactionException.class)
    public final ResponseEntity<Map<String, Object>> handleMethodArgumentNotValidException(SagaTransactionException ex, WebRequest request) {
        while (!sagaTransactionHandler.getTransactionHistory().isEmpty()){
            RemoteCallRequest transaction =  sagaTransactionHandler.getTransactionHistory().pollLast();
            if (transaction != null) {
                if(transaction.getCompensationRequest() instanceof RemoteKafkaCallPackage){
                    //kafka compensation call should be handled here;
                    log.warn("Kafka rollback not implemented");
                }else {
                    remoteCall.callForCompensation(transaction, Boolean.class);
                }
            }

        }

        return new ResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR, ReturnType.FAILURE).withError(ex.getMessage()).build();
    }
}
