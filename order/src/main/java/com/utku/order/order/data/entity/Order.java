package com.utku.order.order.data.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author UTKU APAYDIN
 * @created 15/04/2022 - 10:16
 */
@Data
@Entity
@Table(name = "T_ORDER")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int inventId;
    private int customerId;
    private int count;
    private String status;
}
