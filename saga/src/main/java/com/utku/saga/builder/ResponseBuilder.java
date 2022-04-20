package com.utku.saga.builder;

import com.utku.saga.enumaration.ResponseDataKey;
import com.utku.saga.enumaration.ReturnType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * @author tcuapaydin
 * @created 19/04/2022 - 18:23
 */

public class ResponseBuilder {
    private final HttpStatus resultStatus;
    private final Map<String, Object> result = new HashMap<>();
    private void resetBuilder(){
        result.remove(ResponseDataKey.CONTENT.getKey());
        result.remove(ResponseDataKey.MESSAGE.getKey());
        result.remove(ResponseDataKey.ERROR.getKey());
    }
    public ResponseBuilder(final HttpStatus resultStatus, final ReturnType returnType) {
        this.resultStatus = resultStatus;
        result.put("returnCode", returnType.getCode());
        result.put("returnMessage", returnType.getMessage());
        result.put("isPaginated", false);
    }

    public ResponseBuilder withError(final Exception e) {
        result.put(ResponseDataKey.ERROR.getKey(),e);
        return this;
    }

    public ResponseBuilder withError(final String error) {
        result.put(ResponseDataKey.ERROR.getKey(),error);
        return this;
    }

    public ResponseBuilder withError(final Object o) {
        result.put(ResponseDataKey.ERROR.getKey(),o);
        return this;
    }

    public ResponseBuilder withData(final Object o) {
        result.put(ResponseDataKey.CONTENT.getKey(),o);
        return this;
    }

    public ResponseBuilder withMessage(final Object o) {
        result.put(ResponseDataKey.MESSAGE.getKey(),o);
        return this;
    }

    public ResponseBuilder withPaginatedData(final Object datalist) {
        result.put("isPaginated", true);
        return withData(datalist);
    }

    public ResponseEntity<Map<String, Object>> build(){
        ResponseEntity<Map<String, Object>> response = new ResponseEntity<>(new HashMap<>(result),resultStatus);
        resetBuilder();
        return response;
    }


}
