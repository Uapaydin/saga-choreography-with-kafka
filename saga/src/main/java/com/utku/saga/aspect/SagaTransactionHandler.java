package com.utku.saga.aspect;

import com.utku.saga.model.RemoteCallRequest;
import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tcuapaydin
 * @created 20/04/2022 - 09:14
 */
@Component
@Data
@RequestScope
public class SagaTransactionHandler {

    public SagaTransactionHandler(){
        this.transactionHistory = new ArrayList<>();
    }
    List<RemoteCallRequest> transactionHistory;
}
