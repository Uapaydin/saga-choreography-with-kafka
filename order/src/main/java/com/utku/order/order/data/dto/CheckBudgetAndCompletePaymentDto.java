package com.utku.order.order.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author UTKU APAYDIN
 * @created 15/04/2022 - 13:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckBudgetAndCompletePaymentDto {
    private int customerId;
    private int inventoryId;
    private Long totalPrice;
}
