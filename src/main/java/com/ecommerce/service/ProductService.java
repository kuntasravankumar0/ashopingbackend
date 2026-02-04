package com.ecommerce.service;

import com.ecommerce.entity.Product;
import com.ecommerce.entity.User;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    public List<Product> searchProducts(String query) {
        return productRepository.findByNameContainingIgnoreCase(query);
    }

    public Product createProduct(Product product, String adminEmail) {
        User admin = userRepository.findByEmail(adminEmail).orElseThrow();
        product.setAdmin(admin);
        if (product.getDiscountPercentage() == null)
            product.setDiscountPercentage(0.0);
        if (product.getRating() == null)
            product.setRating(0.0);
        if (product.getRatingCount() == null)
            product.setRatingCount(0);
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, Product productDetails, String adminEmail) {
        Product product = getProductById(id);
        if (!product.getAdmin().getEmail().equals(adminEmail)) {
            throw new RuntimeException("Not authorized to update this product");
        }
        product.setName(productDetails.getName());
        product.setDescription(productDetails.getDescription());
        product.setPrice(productDetails.getPrice());
        product.setDiscountPercentage(productDetails.getDiscountPercentage());
        product.setRating(productDetails.getRating());
        product.setRatingCount(productDetails.getRatingCount());
        product.setCategory(productDetails.getCategory());
        product.setSubCategory(productDetails.getSubCategory());
        product.setImageUrl(productDetails.getImageUrl());
        return productRepository.save(product);
    }

    public List<Product> getMyProducts(String adminEmail) {
        User admin = userRepository.findByEmail(adminEmail).orElseThrow();
        // Self-healing: Link any ownerless products to this admin automatically
        List<Product> allProducts = productRepository.findAll();
        boolean updated = false;
        for (Product p : allProducts) {
            if (p.getAdmin() == null) {
                p.setAdmin(admin);
                updated = true;
            }
        }
        if (updated) {
            productRepository.saveAll(allProducts);
        }
        return productRepository.findByAdminId(admin.getId());
    }

    public void deleteProduct(Long id, String adminEmail) {
        Product product = getProductById(id);
        if (!product.getAdmin().getEmail().equals(adminEmail)) {
            throw new RuntimeException("Not authorized to delete this product");
        }
        productRepository.delete(product);
    }

    public List<String> getAllCategories() {
        return productRepository.findDistinctCategories();
    }
}
