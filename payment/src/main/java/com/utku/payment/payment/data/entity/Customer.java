package com.utku.payment.payment.data.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author UTKU APAYDIN
 * @created 15/04/2022 - 10:22
 */
@Data
@Entity
@Table
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String surname;
    private Long budget;
}
