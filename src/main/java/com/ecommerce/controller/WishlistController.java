package com.ecommerce.controller;

import com.ecommerce.entity.Wishlist;
import com.ecommerce.service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/wishlist")

public class WishlistController {
    @Autowired
    private WishlistService wishlistService;

    @GetMapping
    public List<Wishlist> getWishlist(Authentication auth) {
        return wishlistService.getWishlist(auth.getName());
    }

    @PostMapping("/add/{productId}")
    public Wishlist addToWishlist(@PathVariable Long productId, Authentication auth) {
        return wishlistService.addToWishlist(auth.getName(), productId);
    }

    @DeleteMapping("/{id}")
    public void removeFromWishlist(@PathVariable Long id) {
        wishlistService.removeFromWishlist(id);
    }
}
