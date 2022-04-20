package com.utku.order.order.data.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author UTKU APAYDIN
 * @created 15/04/2022 - 10:51
 */
@Data
@AllArgsConstructor
public class UpdateInventoryForSoldItemRequestDto {
    private int inventoryId;
    private int count;
}
