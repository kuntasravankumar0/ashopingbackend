package com.ecommerce.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "productorders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderId; // Internal readable ID
    private String paymentId;
    private Double totalAmount;
    private Double discount;
    private String status; // START, READY_TO_DELIVERY, COMPLETED

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "order")
    private List<OrderItem> items;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (status == null)
            status = "START";
    }
}
