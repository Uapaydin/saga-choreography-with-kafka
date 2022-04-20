package com.utku.saga.enumaration;

/**
 * @author tcuapaydin
 * @created 19/04/2022 - 18:18
 */
public enum ReturnType {
    SUCCESS(0,"The operation succeeded."),
    FAILURE(-1,"An error occured");

    int code;
    String message;

    ReturnType(int code, String message){
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
