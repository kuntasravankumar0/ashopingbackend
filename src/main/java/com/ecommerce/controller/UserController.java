package com.ecommerce.controller;

import com.ecommerce.entity.User;
import com.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")

public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/profile")
    public User getProfile(Authentication auth) {
        return userRepository.findByEmail(auth.getName()).orElseThrow();
    }

    @PutMapping("/profile")
    public User updateProfile(@RequestBody User updates, Authentication auth) {
        User user = userRepository.findByEmail(auth.getName()).orElseThrow();

        if (updates.getName() != null)
            user.setName(updates.getName());
        if (updates.getMobile() != null)
            user.setMobile(updates.getMobile());
        if (updates.getGender() != null)
            user.setGender(updates.getGender());

        return userRepository.save(user);
    }
}
