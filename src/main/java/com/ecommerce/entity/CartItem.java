package com.ecommerce.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "productcart_items", indexes = {
        @Index(name = "idx_cart_user", columnList = "user_id"),
        @Index(name = "idx_cart_user_product", columnList = "user_id, product_id")
})
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; // Simplifying: One cart per user handled by list of items

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private Integer quantity;
}
