package com.utku.order.order.properties;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author UTKU APAYDIN
 * @created 15/04/2022 - 13:50
 */
@Data
@Component
@ConfigurationProperties(prefix = "services")
public class ServiceProperties {

    private InventoryParams inventory = new InventoryParams();
    private PaymentParams payment = new PaymentParams();

    @Data
    public static class InventoryParams {
        private String url;
        private String updateInventoryForSoldItem;
        private String lockItemForPurchase;

        public String getUpdateInventoryForSoldItemUrl(){
            return url + updateInventoryForSoldItem;
        }
        public String getLockItemForPurchaseUrl(){
            return url + lockItemForPurchase;
        }
    }


    @Data
    public static class PaymentParams {
        private String url;
        private String checkBudgetAndCompletePayment;
        public String getCheckBudgetAndCompletePaymentUrl(){
            return url + checkBudgetAndCompletePayment;
        }
    }


}
