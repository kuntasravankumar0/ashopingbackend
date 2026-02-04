package com.ecommerce.config;

import com.ecommerce.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(List.of(
                            "http://localhost:3000",
                            "http://127.0.0.1:3000",
                            "https://allshoppoint.vercel.app"));

                    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    config.setAllowedHeaders(List.of("*"));
                    config.setAllowCredentials(true);
                    return config;
                }))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/api/auth/**", "/api/public/**", "/api/users/**").permitAll()
                        .requestMatchers("/api/products/my").authenticated()
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/products/**").permitAll()
                        .requestMatchers("/api/admin/apply").authenticated()
                        .requestMatchers("/api/admin/**", "/api/products/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/api/orders/user", "/api/orders/place", "/api/cart/**", "/api/wishlist/**",
                                "/api/address/**")
                        .authenticated()
                        .anyRequest().authenticated())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
