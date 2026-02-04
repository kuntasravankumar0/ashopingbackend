package com.ecommerce.service;

import com.ecommerce.dto.AuthResponse;
import com.ecommerce.dto.GoogleLoginRequest;
import com.ecommerce.entity.User;
import com.ecommerce.repository.UserRepository;
import com.ecommerce.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider tokenProvider;

    public AuthResponse googleLogin(GoogleLoginRequest request) {
        try {
            Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
            User user;

            if (userOpt.isPresent()) {
                user = userOpt.get();
                // Ensure developer emails stay ADMIN in DB
                if ("iloveyoucanyoulovemeha@gmail.com".equals(user.getEmail())
                        || "kunta.sravan11111@gmail.com".equals(user.getEmail())
                        || user.getEmail().toLowerCase().contains("admin")) {
                    if (!"ROLE_ADMIN".equals(user.getRole())) {
                        user.setRole("ROLE_ADMIN");
                        userRepository.save(user);
                    }
                }
            } else {
                // Register new user
                user = new User();
                user.setEmail(request.getEmail());
                user.setName(request.getName());
                user.setGoogleId(request.getGoogleId());
                user.setRole("ROLE_USER");

                // Auto-promote if new
                if ("iloveyoucanyoulovemeha@gmail.com".equals(user.getEmail())
                        || "kunta.sravan11111@gmail.com".equals(user.getEmail())
                        || user.getEmail().toLowerCase().contains("admin")) {
                    user.setRole("ROLE_ADMIN");
                }

                userRepository.save(user);
            }

            String token = tokenProvider.generateToken(user.getEmail(), user.getRole());
            return new AuthResponse(token, user.getEmail(), user.getName(), user.getRole());
        } catch (Exception e) {
            System.err.println("Error during google login process: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}
