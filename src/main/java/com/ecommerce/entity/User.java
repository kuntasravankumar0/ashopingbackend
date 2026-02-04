package com.ecommerce.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "producteshop_users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    private String name;
    private String mobile;
    private String gender;

    // ROLE_USER, ROLE_ADMIN
    private String role;

    private String googleId;
}
