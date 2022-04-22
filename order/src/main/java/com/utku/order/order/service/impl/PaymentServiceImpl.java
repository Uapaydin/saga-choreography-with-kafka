package com.utku.order.order.service.impl;

import com.utku.order.order.data.dto.CheckBudgetAndCompletePaymentDto;
import com.utku.order.order.properties.ServiceProperties;
import com.utku.order.order.service.PaymentService;
import com.utku.saga.enumaration.AuthorizationType;
import com.utku.saga.model.RemoteCallPackage;
import com.utku.saga.model.RemoteCallRequest;
import com.utku.saga.service.RemoteCall;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

/**
 * @author UTKU APAYDIN
 * @created 15/04/2022 - 10:52
 */
@Service
public class PaymentServiceImpl implements PaymentService {

    private final RemoteCall remoteCall;

    private final ServiceProperties serviceProperties;

    public PaymentServiceImpl(ServiceProperties serviceProperties, RemoteCall remoteCall) {
        this.serviceProperties = serviceProperties;
        this.remoteCall = remoteCall;
    }

    @Override
    public boolean checkBudgetAndCompletePayment(CheckBudgetAndCompletePaymentDto checkBudgetAndCompletePaymentDto) {
        RemoteCallRequest remoteCallRequest = new RemoteCallRequest();
        RemoteCallPackage processRequest = new RemoteCallPackage();
        processRequest.setBody(checkBudgetAndCompletePaymentDto);
        processRequest.setAuthorizationType(AuthorizationType.NONE);
        processRequest.setHttpMethod(HttpMethod.POST);
        processRequest.setUrl(serviceProperties.getPayment().getCheckBudgetAndCompletePaymentUrl());
        remoteCallRequest.setProcessRequest(processRequest);
        return remoteCall.callForObject(remoteCallRequest,Boolean.class);
    }
}
