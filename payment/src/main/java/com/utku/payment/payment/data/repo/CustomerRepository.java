package com.utku.payment.payment.data.repo;

import com.utku.payment.payment.data.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author tcuapaydin
 * @created 15/04/2022 - 14:51
 */
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
}
