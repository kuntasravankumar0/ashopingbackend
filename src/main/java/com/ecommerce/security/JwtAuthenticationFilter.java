package com.ecommerce.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.util.StringUtils;

import com.ecommerce.repository.UserRepository;
import com.ecommerce.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String jwt = getJwtFromRequest(request);

            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                String email = tokenProvider.getEmailFromJwt(jwt);
                System.out.println("DEBUG: JWT Valid. Email: " + email);

                // Fetch real-time role from DB
                String role = "ROLE_USER";
                Optional<User> userOpt = userRepository.findByEmail(email);
                if (userOpt.isPresent()) {
                    role = userOpt.get().getRole();
                    System.out.println("DEBUG: Role from DB: " + role);
                } else {
                    System.out.println("DEBUG: User not found in DB.");
                }

                // Super Admin Override
                if ("iloveyoucanyoulovemeha@gmail.com".equals(email)
                        || "kunta.sravan11111@gmail.com".equals(email)
                        || email.toLowerCase().contains("admin")) {
                    System.out.println("DEBUG: Overriding role to ROLE_ADMIN for super user/admin email.");
                    role = "ROLE_ADMIN";
                }

                // Ensure role has ROLE_ prefix for Spring Security
                String finalRole = role;
                if (finalRole != null && !finalRole.startsWith("ROLE_")) {
                    finalRole = "ROLE_" + finalRole;
                }

                SimpleGrantedAuthority authority = new SimpleGrantedAuthority(
                        finalRole != null ? finalRole : "ROLE_USER");

                System.out.println("Extracted User: " + email + " authenticated with Role: " + finalRole);

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        email, null, Collections.singletonList(authority));

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                System.out.println("DEBUG: JWT invalid or empty.");
            }
        } catch (Exception ex) {
            System.err.println("Could not set user authentication in security context");
            ex.printStackTrace();
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
