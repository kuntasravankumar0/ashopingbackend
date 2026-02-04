package com.ecommerce.dto;

import lombok.Data;
import java.util.List;

@Data
public class OrderRequest {
    private Long addressId;
    private String paymentId;
    private Double amount;
    private Double discount;
    private List<OrderItemRequest> items;

    @Data
    public static class OrderItemRequest {
        private Long productId;
        private Integer quantity;
        private Double price;
    }
}
