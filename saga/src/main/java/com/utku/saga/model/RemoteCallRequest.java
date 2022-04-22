package com.utku.saga.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author tcuapaydin
 * @created 21/04/2022 - 13:26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RemoteCallRequest {
    private RemoteCallPackage processRequest;
    private RemoteCallPackage compensationRequest;
}
