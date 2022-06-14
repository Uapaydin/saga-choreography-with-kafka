package com.utku.order.order.service.impl;

import com.utku.order.order.data.dto.CheckBudgetAndCompletePaymentDto;
import com.utku.order.order.properties.ServiceProperties;
import com.utku.order.order.service.PaymentService;
import com.utku.saga.enumaration.AuthorizationType;
import com.utku.saga.model.RemoteCallRequest;
import com.utku.saga.model.RemoteRestCallPackage;
import com.utku.saga.service.RemoteCall;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

/**
 * @author UTKU APAYDIN
 * @created 15/04/2022 - 10:52
 */
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {


    private final RemoteCall remoteCall;
    private final ServiceProperties serviceProperties;

    @Override
    public boolean checkBudgetAndCompletePayment(CheckBudgetAndCompletePaymentDto checkBudgetAndCompletePaymentDto) {
        RemoteCallRequest remoteCallRequest = new RemoteCallRequest();
        RemoteRestCallPackage processRequest = new RemoteRestCallPackage();
        processRequest.setBody(checkBudgetAndCompletePaymentDto);
        processRequest.setAuthorizationType(AuthorizationType.NONE);
        processRequest.setHttpMethod(HttpMethod.POST);
        processRequest.setUrl(serviceProperties.getPayment().getCheckBudgetAndCompletePaymentUrl());
        remoteCallRequest.setProcessRequest(processRequest);
        RemoteRestCallPackage compensationRequest = new RemoteRestCallPackage();
        compensationRequest.setAuthorizationType(AuthorizationType.NONE);
        compensationRequest.setHttpMethod(HttpMethod.POST);
        compensationRequest.setUrl(serviceProperties.getPayment().getCheckBudgetAndCompletePaymentUrl());
        remoteCallRequest.setCompensationRequest(compensationRequest);
        return remoteCall.callForObject(remoteCallRequest,Boolean.class);
    }
}
