package com.ecommerce.controller;

import com.ecommerce.entity.User;
import com.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserManagementController {

    @Autowired
    private UserRepository userRepository;

    @PutMapping("/{userId}/promote-admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> promoteToAdmin(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setRole("ROLE_ADMIN");
        userRepository.save(user);

        return ResponseEntity.ok("User " + user.getEmail() + " promoted to ADMIN successfully!");
    }

    @PutMapping("/{userId}/demote-user")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> demoteToUser(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setRole("ROLE_USER");
        userRepository.save(user);

        return ResponseEntity.ok("User " + user.getEmail() + " demoted to USER successfully!");
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }
}
