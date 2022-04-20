package com.utku.saga.enumaration;

/**
 * @author tcuapaydin
 * @created 19/04/2022 - 18:24
 */
public enum ResponseDataKey {
    CONTENT("content"), MESSAGE("message"), ERROR("error");

    private final String key;

    ResponseDataKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
