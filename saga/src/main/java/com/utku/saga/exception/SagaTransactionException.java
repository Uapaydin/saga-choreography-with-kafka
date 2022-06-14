package com.utku.saga.exception;

import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

/**
 * @author tcuapaydin
 * @created 13/06/2022 - 14:28
 */
public class SagaTransactionException extends BaseException {
    private static final String MESSAGE = "Transaction throw for compensation";
    public SagaTransactionException(HttpStatus httpStatus, String message) {
        super(httpStatus, StringUtils.hasText(message)?message:MESSAGE);
    }

    public SagaTransactionException(HttpStatus httpStatus) {
        super(httpStatus);
    }
}
