package com.utku.order.order.service.Impl;

import com.utku.order.order.data.dto.CheckBudgetAndCompletePaymentDto;
import com.utku.order.order.properties.ServiceProperties;
import com.utku.order.order.service.PaymentService;
import com.utku.saga.enumaration.AuthorizationType;
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
        return remoteCall.callForObject(serviceProperties.getPayment().getCheckBudgetAndCompletePaymentUrl(),
                HttpMethod.POST,Boolean.class, AuthorizationType.NONE,checkBudgetAndCompletePaymentDto);
    }
}
