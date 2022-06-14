package com.utku.inventory.inventory.service.impl;

import com.utku.inventory.inventory.data.dto.InventoryDto;
import com.utku.inventory.inventory.data.dto.LockItemForPurchaseRequestDto;
import com.utku.inventory.inventory.data.dto.UpdateInventoryForSoldItemRequestDto;
import com.utku.inventory.inventory.data.entity.Inventory;
import com.utku.inventory.inventory.data.repo.InventoryRepository;
import com.utku.inventory.inventory.service.InventoryService;
import com.utku.inventory.inventory.util.InventoryStatus;
import com.utku.saga.exception.BaseException;
import com.utku.saga.exception.SagaTransactionException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author tcuapaydin
 * @created 15/04/2022 - 15:13
 */
@Service
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryServiceImpl(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Override
    public InventoryDto lockItemForPurchaseRequestDto(LockItemForPurchaseRequestDto lockItemForPurchaseRequestDto) {
        Optional<Inventory> foundItem = inventoryRepository.findById(lockItemForPurchaseRequestDto.getInventoryId());
        if(foundItem.isPresent()){
            foundItem.get().setStatus(InventoryStatus.INVENTORY_STATUS_LOCKED.getValue());
            inventoryRepository.saveAndFlush(foundItem.get());
        }else{
            throw new BaseException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new InventoryDto(foundItem.get());
    }

    @Override
    public Boolean updateInventoryForSoldItem(UpdateInventoryForSoldItemRequestDto updateInventoryForSoldItemRequestDto) {
        Optional<Inventory> foundItem = inventoryRepository.findById(updateInventoryForSoldItemRequestDto.getInventoryId());
        if(foundItem.isPresent()){
            if(foundItem.get().getCount() < updateInventoryForSoldItemRequestDto.getCount()){
                throw new SagaTransactionException(HttpStatus.INTERNAL_SERVER_ERROR, "Not enough item");
            }
            foundItem.get().setStatus(InventoryStatus.INVENTORY_STATUS_AVAILABLE.getValue());
            foundItem.get().setCount(foundItem.get().getCount() - updateInventoryForSoldItemRequestDto.getCount());
            inventoryRepository.saveAndFlush(foundItem.get());
        }else{
            throw new BaseException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return true;
    }
}
