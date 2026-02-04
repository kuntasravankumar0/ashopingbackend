package com.ecommerce.service;

import com.ecommerce.entity.*;
import com.ecommerce.repository.*;
import com.ecommerce.dto.OrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CartItemRepository cartItemRepository;

    @Transactional
    public Order createOrder(String email, Long addressId, String paymentId, Double amount, Double discount,
            List<OrderRequest.OrderItemRequest> itemRequests) {
        User user = userRepository.findByEmail(email).orElseThrow();
        Address address = addressRepository.findById(addressId).orElseThrow();

        Order order = new Order();
        order.setUser(user);
        order.setAddress(address);
        order.setPaymentId(paymentId);
        order.setTotalAmount(amount);
        order.setDiscount(discount != null ? discount : 0.0);
        order.setOrderId(UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        order.setStatus("START");

        List<OrderItem> items = itemRequests.stream().map(req -> {
            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(productRepository.findById(req.getProductId()).orElseThrow());
            item.setQuantity(req.getQuantity());
            item.setPrice(req.getPrice());
            return item;
        }).collect(Collectors.toList());

        order.setItems(items);
        Order savedOrder = orderRepository.save(order);

        // Clear user's cart after order success
        cartItemRepository.deleteByUserId(user.getId());

        return savedOrder;
    }

    public List<Order> getUserOrders(String email) {
        return userRepository.findByEmail(email)
                .map(user -> orderRepository.findByUserId(user.getId()))
                .orElse(List.of()); // Return empty list if user not found (stale token case)
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Transactional
    public List<Order> getOrdersForAdmin(String adminEmail) {
        User admin = userRepository.findByEmail(adminEmail).orElseThrow();

        // --- Global Claim Logic ---
        // --- Global Claim Logic ---
        // Claim all ownerless products for this admin (Optimized)
        productRepository.claimAuthoredProducts(admin);
        // --------------------------

        return orderRepository.findByAdminId(admin.getId());
    }

    public Order updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId).orElseThrow();
        order.setStatus(status);
        return orderRepository.save(order);
    }
}
