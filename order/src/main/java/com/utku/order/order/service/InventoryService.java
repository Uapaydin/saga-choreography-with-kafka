package com.utku.order.order.service;

import com.utku.order.order.data.dto.request.LockItemForPurchaseRequestDto;
import com.utku.order.order.data.dto.request.UpdateInventoryForSoldItemRequestDto;
import com.utku.order.order.data.dto.response.ItemDto;

/**
 * @author UTKU APAYDIN
 * @created 15/04/2022 - 10:41
 */
public interface InventoryService {
    ItemDto lockItemForPurchase(LockItemForPurchaseRequestDto lockItemForPPurchaseRequestDto);
    boolean updateInventoryForSoldItem(UpdateInventoryForSoldItemRequestDto updateInventoryForSoldItemRequestDto);
}
