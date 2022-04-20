package com.utku.inventory.inventory.data.repo;

import com.utku.inventory.inventory.data.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author tcuapaydin
 * @created 19/04/2022 - 10:20
 */
public interface InventoryRepository extends JpaRepository<Inventory, Integer> {
}
