package com.utku.saga.exception;

import org.springframework.http.HttpStatus;

/**
 * @author tcuapaydin
 * @created 19/04/2022 - 18:20
 */
public class AuthorizationException extends BaseException
{
    private static final HttpStatus HTTP_STATUS = HttpStatus.UNAUTHORIZED;
    private static final String MESSAGE = "Unauthorized access.";

    public AuthorizationException() {
        super(HTTP_STATUS, MESSAGE);
    }

    public AuthorizationException(String message) {
        super(HTTP_STATUS, message);
    }
}
