package com.utku.order.order.data.repo;

import com.utku.order.order.data.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author UTKU APAYDIN
 * @created 15/04/2022 - 11:29
 */
public interface OrderRepository extends JpaRepository<Order, Integer> {
}
