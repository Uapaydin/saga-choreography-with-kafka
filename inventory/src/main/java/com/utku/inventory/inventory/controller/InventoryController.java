package com.utku.inventory.inventory.controller;

import com.utku.inventory.inventory.data.dto.LockItemForPurchaseRequestDto;
import com.utku.inventory.inventory.data.dto.UpdateInventoryForSoldItemRequestDto;
import com.utku.inventory.inventory.service.InventoryService;
import com.utku.saga.builder.ResponseBuilder;
import com.utku.saga.enumaration.ReturnType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author UTKU APAYDIN
 * @created 15/04/2022 - 10:31
 */
@RestController
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PostMapping("/v1/lock/purchase/item")
    public ResponseEntity<Map<String,Object>> lockItemForPurchaseRequestDto(@RequestBody LockItemForPurchaseRequestDto lockItemForPurchaseRequestDto){
        ResponseBuilder responseBuilder = new ResponseBuilder(HttpStatus.OK, ReturnType.SUCCESS);
        return responseBuilder.withData(inventoryService.lockItemForPurchaseRequestDto(lockItemForPurchaseRequestDto)).build();
    }
    @PostMapping("/v1/item/sold")
    public ResponseEntity<Map<String,Object>> updateInventoryForSoldItem(@RequestBody UpdateInventoryForSoldItemRequestDto updateInventoryForSoldItemRequestDto){
        ResponseBuilder responseBuilder = new ResponseBuilder(HttpStatus.OK, ReturnType.SUCCESS);
        return responseBuilder.withData(inventoryService.updateInventoryForSoldItem(updateInventoryForSoldItemRequestDto)).build();
    }


}
