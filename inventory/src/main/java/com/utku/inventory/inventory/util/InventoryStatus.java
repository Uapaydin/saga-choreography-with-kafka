package com.utku.inventory.inventory.util;

import com.utku.inventory.inventory.service.InventoryService;
import lombok.Getter;

/**
 * @author tcuapaydin
 * @created 19/04/2022 - 10:26
 */
@Getter
public enum InventoryStatus {
    INVENTORY_STATUS_AVAILABLE("available"),
    INVENTORY_STATUS_LOCKED("locked");

    private String value;

    InventoryStatus(String value){
        this.value = value;
    }
}
