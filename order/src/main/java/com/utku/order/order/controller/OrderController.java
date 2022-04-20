package com.utku.order.order.controller;

import com.utku.order.order.data.dto.request.CreateOrderRequestDto;
import com.utku.order.order.service.OrderService;
import com.utku.saga.builder.ResponseBuilder;
import com.utku.saga.enumaration.ReturnType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author UTKU APAYDIN
 * @created 15/04/2022 - 10:31
 */
@RestController
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/v1/order")
    public ResponseEntity<Map<String, Object>> createOrder(@RequestBody CreateOrderRequestDto createOrderRequest){
        ResponseBuilder responseBuilder = new ResponseBuilder(HttpStatus.CREATED, ReturnType.SUCCESS);
        return responseBuilder.withData(orderService.startOrderCreation(createOrderRequest)).build();
    }

}
