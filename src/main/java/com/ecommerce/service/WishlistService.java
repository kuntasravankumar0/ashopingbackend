package com.ecommerce.service;

import com.ecommerce.entity.Wishlist;
import com.ecommerce.entity.User;
import com.ecommerce.repository.WishlistRepository;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class WishlistService {
    @Autowired
    private WishlistRepository wishlistRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;

    public List<Wishlist> getWishlist(String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        return wishlistRepository.findByUserId(user.getId());
    }

    public Wishlist addToWishlist(String email, Long productId) {
        User user = userRepository.findByEmail(email).orElseThrow();
        Wishlist wishlist = new Wishlist();
        wishlist.setUser(user);
        wishlist.setProduct(productRepository.findById(productId).orElseThrow());
        return wishlistRepository.save(wishlist);
    }

    public void removeFromWishlist(Long id) {
        wishlistRepository.deleteById(id);
    }
}
