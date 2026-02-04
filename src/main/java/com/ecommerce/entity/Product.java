package com.ecommerce.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "productproducts")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(length = 1000)
    private String description;

    private Double price;
    private Double discountPercentage; // e.g. 10.0 for 10%
    private Double rating; // average rating
    private Integer ratingCount;
    private String category;
    private String subCategory;

    @Column(columnDefinition = "TEXT")
    private String imageUrl; // Can be Base64 or URL

    // The admin who created this product
    @ManyToOne
    @JoinColumn(name = "admin_id")
    private User admin;
}
