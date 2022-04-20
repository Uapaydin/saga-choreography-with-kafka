package com.utku.payment.payment.data.repo;

import com.utku.payment.payment.data.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author tcuapaydin
 * @created 15/04/2022 - 14:52
 */
public interface PaymentRepository extends JpaRepository<Payment, Integer> {
}
