package com.ecommerce.repository;

import com.ecommerce.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @org.springframework.data.jpa.repository.Query("SELECT DISTINCT o FROM Order o LEFT JOIN FETCH o.items i LEFT JOIN FETCH i.product p WHERE o.user.id = :userId")
    List<Order> findByUserId(Long userId);

    @org.springframework.data.jpa.repository.Query("SELECT DISTINCT o FROM Order o LEFT JOIN FETCH o.user LEFT JOIN FETCH o.address JOIN o.items i JOIN i.product p JOIN p.admin a WHERE a.id = :adminId")
    List<Order> findByAdminId(Long adminId);
}
