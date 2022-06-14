package com.utku.order.order.service.impl;

import com.utku.order.order.data.dto.request.LockItemForPurchaseRequestDto;
import com.utku.order.order.data.dto.request.UpdateInventoryForSoldItemRequestDto;
import com.utku.order.order.data.dto.response.ItemDto;
import com.utku.order.order.properties.ServiceProperties;
import com.utku.order.order.service.InventoryService;
import com.utku.saga.enumaration.AuthorizationType;
import com.utku.saga.model.RemoteCallRequest;
import com.utku.saga.model.RemoteRestCallPackage;
import com.utku.saga.service.RemoteCall;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

/**
 * @author UTKU APAYDIN
 * @created 15/04/2022 - 10:51
 */
@Service
@AllArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final RemoteCall remoteCall;
    private final ServiceProperties serviceProperties;


    @Override
    public ItemDto lockItemForPurchase(LockItemForPurchaseRequestDto lockItemForPurchaseRequestDto) {
        RemoteCallRequest remoteCallRequest = new RemoteCallRequest();
        RemoteRestCallPackage processRequest = new RemoteRestCallPackage();
        processRequest.setBody(lockItemForPurchaseRequestDto);
        processRequest.setAuthorizationType(AuthorizationType.NONE);
        processRequest.setHttpMethod(HttpMethod.POST);
        processRequest.setUrl(serviceProperties.getInventory().getLockItemForPurchaseUrl());
        remoteCallRequest.setProcessRequest(processRequest);
        return remoteCall.callForObject(remoteCallRequest,ItemDto.class);
    }

    @Override
    public boolean updateInventoryForSoldItem(UpdateInventoryForSoldItemRequestDto updateInventoryForSoldItemRequestDto) {
        RemoteCallRequest remoteCallRequest = new RemoteCallRequest();
        RemoteRestCallPackage processRequest = new RemoteRestCallPackage();
        processRequest.setBody(updateInventoryForSoldItemRequestDto);
        processRequest.setAuthorizationType(AuthorizationType.NONE);
        processRequest.setHttpMethod(HttpMethod.POST);
        processRequest.setUrl(serviceProperties.getInventory().getUpdateInventoryForSoldItemUrl());
        remoteCallRequest.setProcessRequest(processRequest);
        return remoteCall.callForObject(remoteCallRequest,Boolean.class);
    }
}
