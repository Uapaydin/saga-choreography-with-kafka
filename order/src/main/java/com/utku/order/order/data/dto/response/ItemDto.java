package com.utku.order.order.data.dto.response;

import lombok.Data;

/**
 * @author UTKU APAYDIN
 * @created 15/04/2022 - 13:04
 */
@Data
public class ItemDto {
    private int id;
    private String name;
    private String status;
    private int count;
    private Long price;
}
