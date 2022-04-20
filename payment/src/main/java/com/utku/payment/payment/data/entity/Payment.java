package com.utku.payment.payment.data.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author UTKU APAYDIN
 * @created 15/04/2022 - 10:25
 */
@Data
@Entity
@Table
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int customerId;
    private int inventoryId;
    private Long totalPrice;
}
