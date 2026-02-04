package com.ecommerce.repository;

import com.ecommerce.entity.AdminApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AdminApplicationRepository extends JpaRepository<AdminApplication, Long> {
    Optional<AdminApplication> findByUserId(Long userId);
}
