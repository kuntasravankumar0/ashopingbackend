package com.ecommerce.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "productwishlist", indexes = {
        @Index(name = "idx_wishlist_user", columnList = "user_id"),
        @Index(name = "idx_wishlist_user_product", columnList = "user_id, product_id")
})
public class Wishlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
