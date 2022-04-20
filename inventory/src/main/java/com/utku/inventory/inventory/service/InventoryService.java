package com.utku.inventory.inventory.service;

import com.utku.inventory.inventory.data.dto.InventoryDto;
import com.utku.inventory.inventory.data.dto.LockItemForPurchaseRequestDto;
import com.utku.inventory.inventory.data.dto.UpdateInventoryForSoldItemRequestDto;

/**
 * @author tcuapaydin
 * @created 15/04/2022 - 15:13
 */
public interface InventoryService {
    InventoryDto lockItemForPurchaseRequestDto(LockItemForPurchaseRequestDto lockItemForPurchaseRequestDto);

    Boolean updateInventoryForSoldItem(UpdateInventoryForSoldItemRequestDto updateInventoryForSoldItemRequestDto);
}
