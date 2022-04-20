package com.utku.saga.dto;

import lombok.Data;

/**
 * @author tcuapaydin
 * @created 19/04/2022 - 18:22
 */
@Data
public class RemoteCallDTO<T> {

    private  int returnCode;
    private String returnMessage;
    private String error;
    private Boolean isPaginated;
    private T content;

}
