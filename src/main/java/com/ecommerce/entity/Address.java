package com.ecommerce.entity;

import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Data
@Table(name = "productaddresses")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    private String addressLine;
    private String city;
    private String state;
    private String pincode;
    private String mobileNumber; // Shipping contact
    private boolean isDefault;
}
