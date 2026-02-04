package com.ecommerce.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "productadmin_applications")
public class AdminApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // PENDING, APPROVED, REJECTED
    private String status;
}
