package com.ecommerce.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/health")
public class HealthController {
    @GetMapping
    public String health() {
        return "Backend is running!";
    }
}
