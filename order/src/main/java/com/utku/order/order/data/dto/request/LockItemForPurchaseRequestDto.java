package com.utku.order.order.data.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author UTKU APAYDIN
 * @created 15/04/2022 - 10:50
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LockItemForPurchaseRequestDto {
    private int inventoryId;
    private int count;
}
