package com.ecommerce.controller;

import com.ecommerce.dto.AuthResponse;
import com.ecommerce.dto.GoogleLoginRequest;
import com.ecommerce.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/google-login")
    public ResponseEntity<AuthResponse> googleLogin(@RequestBody GoogleLoginRequest request) {
        System.out.println("Received google login request for: " + request.getEmail());
        return ResponseEntity.ok(authService.googleLogin(request));
    }

}
