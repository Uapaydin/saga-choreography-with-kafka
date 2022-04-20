package com.utku.order.order.service;

import com.utku.order.order.data.dto.request.CreateOrderRequestDto;

/**
 * @author UTKU APAYDIN
 * @created 15/04/2022 - 10:36
 */
public interface OrderService {

    String startOrderCreation(CreateOrderRequestDto createOrderRequestDto);
}
