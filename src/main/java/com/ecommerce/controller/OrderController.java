package com.ecommerce.controller;

import com.ecommerce.entity.Order;
import com.ecommerce.dto.OrderRequest;
import com.ecommerce.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/orders")

public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/place")
    public Order placeOrder(@RequestBody OrderRequest request, Authentication auth) {
        return orderService.createOrder(
                auth.getName(),
                request.getAddressId(),
                request.getPaymentId(),
                request.getAmount(),
                request.getDiscount(),
                request.getItems());
    }

    @GetMapping("/user")
    public List<Order> getUserOrders(Authentication auth) {
        return orderService.getUserOrders(auth.getName());
    }

    @GetMapping("/all")
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @PutMapping("/{id}/status")
    public Order updateStatus(@PathVariable Long id, @RequestParam String status) {
        return orderService.updateOrderStatus(id, status);
    }
}
