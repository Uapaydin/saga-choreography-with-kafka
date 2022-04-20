package com.utku.order.order.service;

import com.utku.order.order.data.dto.CheckBudgetAndCompletePaymentDto;

/**
 * @created 15/04/2022 - 10:40
 * @author UTKU APAYDIN
 */
public interface PaymentService {
    boolean checkBudgetAndCompletePayment(CheckBudgetAndCompletePaymentDto checkBudgetAndCompletePaymentDto);
}
