package com.utku.order.order.data.dto.request;

import lombok.Data;

/**
 * @author UTKU APAYDIN
 * @created 15/04/2022 - 10:33
 */
@Data
public class CreateOrderRequestDto {
    private int customerId;
    private int inventoryId;
    private int count;
}
