package com.utku.payment.payment.controller;

import com.utku.payment.payment.data.dto.CheckBudgetAndCompletePaymentDto;
import com.utku.payment.payment.service.PaymentService;
import com.utku.saga.builder.ResponseBuilder;
import com.utku.saga.enumaration.ReturnType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author tcuapaydin
 * @created 15/04/2022 - 14:45
 */
@RestController
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/v1/check/budget/complete/payment")
    public ResponseEntity<Map<String, Object>> checkBudgetAndCompletePayment(@RequestBody CheckBudgetAndCompletePaymentDto checkBudgetAndCompletePaymentDto){
        ResponseBuilder responseBuilder = new ResponseBuilder(HttpStatus.OK, ReturnType.SUCCESS);
        paymentService.checkBudgetAndCompletePayment(checkBudgetAndCompletePaymentDto);
        return responseBuilder.withData(true).build();
    }
}
