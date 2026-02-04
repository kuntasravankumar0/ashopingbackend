package com.ecommerce.controller;

import com.ecommerce.entity.CartItem;
import com.ecommerce.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/cart")

public class CartController {
    @Autowired
    private CartService cartService;

    @GetMapping
    public List<CartItem> getCart(Authentication auth) {
        return cartService.getCart(auth.getName());
    }

    @PostMapping("/add/{productId}")
    public CartItem addToCart(@PathVariable Long productId, @RequestParam(defaultValue = "1") int quantity,
            Authentication auth) {
        return cartService.addToCart(auth.getName(), productId, quantity);
    }

    @PutMapping("/{id}")
    public CartItem updateQuantity(@PathVariable Long id, @RequestParam int quantity) {
        return cartService.updateQuantity(id, quantity);
    }

    @DeleteMapping("/{id}")
    public void removeFromCart(@PathVariable Long id) {
        cartService.removeFromCart(id);
    }
}
