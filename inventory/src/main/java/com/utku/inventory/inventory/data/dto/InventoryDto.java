package com.utku.inventory.inventory.data.dto;

import com.utku.inventory.inventory.data.entity.Inventory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

/**
 * @author tcuapaydin
 * @created 15/04/2022 - 15:25
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryDto {
    private int id;
    private String name;
    private String status;
    private int count;
    private int price;

    public InventoryDto(Inventory inventory) {
        BeanUtils.copyProperties(inventory,this);
    }
}
