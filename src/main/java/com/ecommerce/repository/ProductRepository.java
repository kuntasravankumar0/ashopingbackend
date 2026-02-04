package com.ecommerce.repository;

import com.ecommerce.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory(String category);

    List<Product> findByAdminId(Long adminId);

    List<Product> findByNameContainingIgnoreCase(String name);

    @org.springframework.data.jpa.repository.Query("SELECT DISTINCT p.category FROM Product p")
    List<String> findDistinctCategories();

    @org.springframework.data.jpa.repository.Modifying
    @org.springframework.transaction.annotation.Transactional
    @org.springframework.data.jpa.repository.Query("UPDATE Product p SET p.admin = :admin WHERE p.admin IS NULL")
    void claimAuthoredProducts(com.ecommerce.entity.User admin);
}
