package com.ecommerce.service;

import com.ecommerce.entity.AdminApplication;
import com.ecommerce.entity.User;
import com.ecommerce.repository.AdminApplicationRepository;
import com.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AdminService {
    @Autowired
    private AdminApplicationRepository adminAppRepo;
    @Autowired
    private UserRepository userRepository;

    public AdminApplication applyForAdmin(String email) {
        User user = userRepository.findByEmail(email).orElseThrow();

        // Check if user already has a pending application
        List<AdminApplication> existing = adminAppRepo.findAll();
        boolean hasPending = existing.stream()
                .anyMatch(a -> a.getUser().getId().equals(user.getId()) && "PENDING".equals(a.getStatus()));

        if (hasPending) {
            throw new RuntimeException("Application already pending approval.");
        }

        AdminApplication app = new AdminApplication();
        app.setUser(user);
        app.setStatus("PENDING");
        return adminAppRepo.save(app);
    }

    public List<AdminApplication> getAllApplications() {
        return adminAppRepo.findAll();
    }

    public void approveApplication(Long applicationId) {
        AdminApplication app = adminAppRepo.findById(applicationId).orElseThrow();
        app.setStatus("APPROVED");
        User user = app.getUser();
        user.setRole("ROLE_ADMIN");
        userRepository.save(user);
        adminAppRepo.save(app);
    }

    public void rejectApplication(Long applicationId) {
        AdminApplication app = adminAppRepo.findById(applicationId).orElseThrow();
        app.setStatus("REJECTED");
        adminAppRepo.save(app);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void demoteAdmin(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        // Prevent demoting the super admin (assuming devs are super admins)
        if ("iloveyoucanyoulovemeha@gmail.com".equals(user.getEmail())) {
            throw new RuntimeException("Super Admin cannot be demoted.");
        }
        user.setRole("ROLE_USER");
        userRepository.save(user);
    }

    public void promoteUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        user.setRole("ROLE_ADMIN");
        userRepository.save(user);
    }
}
