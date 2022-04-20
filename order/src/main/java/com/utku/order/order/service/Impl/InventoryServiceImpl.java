package com.utku.order.order.service.Impl;

import com.utku.order.order.data.dto.request.LockItemForPurchaseRequestDto;
import com.utku.order.order.data.dto.request.UpdateInventoryForSoldItemRequestDto;
import com.utku.order.order.data.dto.response.ItemDto;
import com.utku.order.order.properties.ServiceProperties;
import com.utku.order.order.service.InventoryService;
import com.utku.saga.enumaration.AuthorizationType;
import com.utku.saga.service.RemoteCall;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

/**
 * @author UTKU APAYDIN
 * @created 15/04/2022 - 10:51
 */
@Service
public class InventoryServiceImpl implements InventoryService {

    private final RemoteCall remoteCall;

    private final ServiceProperties serviceProperties;

    public InventoryServiceImpl(ServiceProperties serviceProperties, RemoteCall remoteCall) {
        this.serviceProperties = serviceProperties;
        this.remoteCall = remoteCall;
    }

    @Override
    public ItemDto lockItemForPurchase(LockItemForPurchaseRequestDto lockItemForPurchaseRequestDto) {
        return remoteCall.callForObject(serviceProperties.getInventory().getLockItemForPurchaseUrl(),
                HttpMethod.POST,ItemDto.class, AuthorizationType.NONE, lockItemForPurchaseRequestDto);
    }

    @Override
    public boolean updateInventoryForSoldItem(UpdateInventoryForSoldItemRequestDto updateInventoryForSoldItemRequestDto) {
        return remoteCall.callForObject(serviceProperties.getInventory().getUpdateInventoryForSoldItemUrl(),
                HttpMethod.POST,Boolean.class, AuthorizationType.NONE, updateInventoryForSoldItemRequestDto);
    }
}
