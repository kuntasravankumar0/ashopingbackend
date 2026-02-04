package com.ecommerce.controller;

import com.ecommerce.entity.AdminApplication;
import com.ecommerce.entity.Order;
import com.ecommerce.entity.User;
import com.ecommerce.service.AdminService;
import com.ecommerce.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import com.ecommerce.security.JwtTokenProvider;
import org.springframework.http.ResponseEntity;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")

public class AdminController {
    @Autowired
    private AdminService adminService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @PostMapping("/apply")
    public ResponseEntity<?> apply(Authentication auth) {
        AdminApplication app = adminService.applyForAdmin(auth.getName());
        Map<String, Object> response = new HashMap<>();
        response.put("application", app);

        if ("APPROVED".equals(app.getStatus())) {
            String token = tokenProvider.generateToken(auth.getName(), "ROLE_ADMIN");
            response.put("token", token);
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/applications")
    public List<AdminApplication> getApplications(Authentication auth) {
        System.out.println(
                "User " + auth.getName() + " accessing applications with authorities: " + auth.getAuthorities());
        return adminService.getAllApplications();
    }

    @PutMapping("/applications/{id}")
    public void updateApplication(@PathVariable Long id, @RequestParam String status, Authentication auth) {
        System.out.println("User " + auth.getName() + " updating application " + id + " to " + status);
        if ("APPROVED".equals(status)) {
            adminService.approveApplication(id);
        } else if ("REJECTED".equals(status)) {
            adminService.rejectApplication(id);
        }
    }

    @GetMapping("/orders")
    public List<Order> getAllOrders(Authentication auth) {
        System.out.println("User " + auth.getName() + " fetching admin orders. Authorities: " + auth.getAuthorities());
        return orderService.getOrdersForAdmin(auth.getName());
    }

    @PutMapping("/orders/{id}/status")
    public Order updateOrderStatus(@PathVariable Long id, @RequestParam String status, Authentication auth) {
        System.out.println("User " + auth.getName() + " updating order " + id + " status to " + status);
        return orderService.updateOrderStatus(id, status);
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return adminService.getAllUsers();
    }

    @PutMapping("/users/{id}/promote")
    public void promote(@PathVariable Long id) {
        adminService.promoteUser(id);
    }

    @PutMapping("/users/{id}/demote")
    public void demote(@PathVariable Long id) {
        adminService.demoteAdmin(id);
    }
}
