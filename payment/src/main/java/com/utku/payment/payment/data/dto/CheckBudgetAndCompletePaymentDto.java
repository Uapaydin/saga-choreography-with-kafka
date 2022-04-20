package com.utku.payment.payment.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author tcuapaydin
 * @created 15/04/2022 - 14:46
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckBudgetAndCompletePaymentDto {
    private int customerId;
    private int inventoryId;
    private Long totalPrice;
}
