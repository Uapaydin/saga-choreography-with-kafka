package com.utku.saga.enumaration;

/**
 * @author tcuapaydin
 * @created 19/04/2022 - 18:20
 */
public enum LogKey {
    REMOTE_ADDRESS("remote-address"),
    REMOTE_PORT("remote-port"),
    REMOTE_HOST("remote-host"),
    FORWARDED_ADDRESS( "x-forwarded-for"),
    USER_ID("user-id"),
    HOST_IP ("hots-ip"),
    REQUEST_ID("request-id"),
    DEALER_ID("dealer-id"),
    VENDOR_ID("vendor-id");

    private final String value;

    LogKey(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
