package com.utku.payment.payment.service;

import com.utku.payment.payment.data.dto.CheckBudgetAndCompletePaymentDto;

/**
 * @author tcuapaydin
 * @created 15/04/2022 - 14:48
 */
public interface PaymentService {
    void checkBudgetAndCompletePayment(CheckBudgetAndCompletePaymentDto checkBudgetAndCompletePaymentDto);
}
