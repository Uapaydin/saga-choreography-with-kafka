package com.utku.saga.model;

import com.utku.saga.enumaration.AuthorizationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.util.MultiValueMap;

/**
 * @author tcuapaydin
 * @created 21/04/2022 - 13:23
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RemoteCallPackage{
    private String url;
    Object body;
    private HttpMethod httpMethod;
    AuthorizationType authorizationType;
    MultiValueMap<String, String> headers;
}
