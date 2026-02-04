package com.ecommerce.dto;

import lombok.Data;

@Data
public class GoogleLoginRequest {
    private String email;
    private String name;
    private String googleId;
    private String photoUrl;
}
