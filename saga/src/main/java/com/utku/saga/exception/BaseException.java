package com.utku.saga.exception;

import org.springframework.http.HttpStatus;

/**
 * @author tcuapaydin
 * @created 19/04/2022 - 18:19
 */
public class BaseException extends RuntimeException {
    private final HttpStatus httpStatus;

    public BaseException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public BaseException(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
