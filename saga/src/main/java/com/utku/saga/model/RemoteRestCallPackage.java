package com.utku.saga.model;

import com.utku.saga.enumaration.AuthorizationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.util.MultiValueMap;

/**
 * @author tcuapaydin
 * @created 13/06/2022 - 14:27
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RemoteRestCallPackage extends RemoteCallPackage{
    private String url;
    private Object body;
    private HttpMethod httpMethod;
    private String jwt;
    private AuthorizationType authorizationType;
    private MultiValueMap<String, String> headers;
}
