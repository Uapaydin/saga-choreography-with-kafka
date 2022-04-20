package com.utku.inventory.inventory.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author tcuapaydin
 * @created 15/04/2022 - 15:22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LockItemForPurchaseRequestDto {
    private int inventoryId;
    private int count;

}
