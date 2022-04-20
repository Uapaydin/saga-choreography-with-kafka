package com.utku.saga.prop;

import com.google.gson.Gson;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.List;
/**
 * @author tcuapaydin
 * @created 19/04/2022 - 18:10
 */
@Component
@Data
@Order(Integer.MIN_VALUE)
@ConfigurationProperties(prefix = "jwt.validation")
public class JwtValidationProperties {

    private Boolean active = false;
    private String jwksUrl;
    private List<String> permittedApis;
    private Boolean generateServiceToken =false;
    private String tokenKey;
    private String decoderServiceUrl;
    private String serviceList;

    @Data
    public static class Service{
        private String serviceName;
        private String token;
    }

    public List<Service> generateServiceList(){
        Gson gson = new Gson();
        return Arrays.asList(gson.fromJson(this.serviceList, Service[].class));
    }
}
