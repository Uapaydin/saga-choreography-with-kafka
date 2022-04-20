package com.utku.order.order.util;


import lombok.Getter;

/**
 * @author UTKU APAYDIN
 * @created 15/04/2022 - 11:20
 */
@Getter
public enum OrderStatus {
    ORDER_CREATED("CREATED"),
    ORDER_PAYMENT_IN_PROGRESS("PAYMENT_IN_PROGRESS"),
    ORDER_COMPLETED("COMPLETED"),
    ORDER_FAILED("FAILED");
    private final String value;

    OrderStatus(String value){
        this.value = value;
    }
}
