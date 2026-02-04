package com.ecommerce.service;

import com.ecommerce.entity.*;
import com.ecommerce.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;

    public List<CartItem> getCart(String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        return cartItemRepository.findByUserId(user.getId());
    }

    public CartItem addToCart(String email, Long productId, int quantity) {
        User user = userRepository.findByEmail(email).orElseThrow();
        Optional<CartItem> existing = cartItemRepository.findByUserIdAndProductId(user.getId(), productId);

        if (existing.isPresent()) {
            CartItem item = existing.get();
            item.setQuantity(item.getQuantity() + quantity);
            return cartItemRepository.save(item);
        } else {
            CartItem item = new CartItem();
            item.setUser(user);
            item.setProduct(productRepository.findById(productId).orElseThrow());
            item.setQuantity(quantity);
            return cartItemRepository.save(item);
        }
    }

    public CartItem updateQuantity(Long id, int quantity) {
        CartItem item = cartItemRepository.findById(id).orElseThrow();
        item.setQuantity(quantity);
        return cartItemRepository.save(item);
    }

    public void removeFromCart(Long id) {
        cartItemRepository.deleteById(id);
    }

}
