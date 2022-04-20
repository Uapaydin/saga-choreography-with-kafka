package com.utku.saga.exception;

import org.springframework.http.HttpStatus;

/**
 * @author tcuapaydin
 * @created 19/04/2022 - 18:19
 */
public class RemoteCallException extends BaseException
{
    private static final HttpStatus HTTP_STATUS = HttpStatus.INTERNAL_SERVER_ERROR;

    public RemoteCallException(String message) {
        super(HTTP_STATUS, message);
    }
}
