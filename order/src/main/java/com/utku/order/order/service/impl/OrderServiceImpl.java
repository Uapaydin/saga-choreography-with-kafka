package com.utku.order.order.service.impl;

import com.utku.order.order.data.dto.CheckBudgetAndCompletePaymentDto;
import com.utku.order.order.data.dto.request.CreateOrderRequestDto;
import com.utku.order.order.data.dto.request.LockItemForPurchaseRequestDto;
import com.utku.order.order.data.dto.request.UpdateInventoryForSoldItemRequestDto;
import com.utku.order.order.data.dto.response.ItemDto;
import com.utku.order.order.data.entity.Order;
import com.utku.order.order.data.repo.OrderRepository;
import com.utku.order.order.service.InventoryService;
import com.utku.order.order.service.OrderService;
import com.utku.order.order.service.PaymentService;
import com.utku.order.order.util.OrderStatus;
import com.utku.saga.aspect.SagaTransactionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author UTKU APAYDIN
 * @created 15/04/2022 - 10:37
 */
@Service
public class OrderServiceImpl implements OrderService {


    @Autowired
    SagaTransactionHandler sagaTransactionHandler;

    private final OrderRepository orderRepository;

    private final InventoryService inventoryService;
    private final PaymentService paymentService;

    public OrderServiceImpl(InventoryService inventoryService, PaymentService paymentService, OrderRepository orderRepository) {
        this.inventoryService = inventoryService;
        this.paymentService = paymentService;
        this.orderRepository = orderRepository;
    }

    @Override
    public String startOrderCreation(CreateOrderRequestDto createOrderRequestDto) {
        Order newOrder;
        //TODO: check inventory status and lock inventory
        ItemDto lockedItem = inventoryService.lockItemForPurchase(new LockItemForPurchaseRequestDto(createOrderRequestDto.getInventoryId(),createOrderRequestDto.getCount()));
        //TODO: CREATE ORDER
        if(lockedItem != null){
            newOrder = createOrder(createOrderRequestDto);
            if(newOrder!=null){
                //TODO: check budget and complete payment
                CheckBudgetAndCompletePaymentDto checkBudgetAndCompletePaymentDto = new CheckBudgetAndCompletePaymentDto(
                        createOrderRequestDto.getCustomerId(),
                        lockedItem.getId(),
                        Math.multiplyExact(createOrderRequestDto.getCount(), lockedItem.getPrice()));
                paymentService.checkBudgetAndCompletePayment(checkBudgetAndCompletePaymentDto);
                //TODO: update order status
                setOrderStatus(newOrder, OrderStatus.ORDER_PAYMENT_IN_PROGRESS);
                //TODO: update inventory status
                UpdateInventoryForSoldItemRequestDto updateInventoryForSoldItemRequestDto =
                        new UpdateInventoryForSoldItemRequestDto(lockedItem.getId(), createOrderRequestDto.getCount());
                inventoryService.updateInventoryForSoldItem(updateInventoryForSoldItemRequestDto);
                //TODO: update order status
                setOrderStatus(newOrder, OrderStatus.ORDER_COMPLETED);
                return "Order purchase completed";
            }
        }
        return "-1";
    }

    private void setOrderStatus(Order newOrder, OrderStatus orderStatus ) {
        newOrder.setStatus(orderStatus.getValue());
        orderRepository.saveAndFlush(newOrder);
    }

    private Order createOrder(CreateOrderRequestDto createOrderRequestDto){
        Order newOrder = new Order();
        newOrder.setCount(createOrderRequestDto.getCount());
        newOrder.setStatus(OrderStatus.ORDER_CREATED.getValue());
        newOrder.setInventId(createOrderRequestDto.getInventoryId());
        newOrder.setCustomerId(createOrderRequestDto.getCustomerId());
        orderRepository.saveAndFlush(newOrder);
        return newOrder;
    }
}
