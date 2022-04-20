package com.utku.inventory.inventory.data.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author UTKU APAYDIN
 * @created 15/04/2022 - 09:26
 */
@Data
@Entity
@Table
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String status;
    @Column(nullable = false)
    private int count;
    @Column(nullable = false)
    private int price;

}
