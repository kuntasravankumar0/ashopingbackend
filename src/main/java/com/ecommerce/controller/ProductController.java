package com.ecommerce.controller;

import com.ecommerce.entity.Product;
import com.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/products")

public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public List<Product> getAllProducts(@RequestParam(required = false) String category,
            @RequestParam(required = false) String search) {
        if (search != null)
            return productService.searchProducts(search);
        if (category != null)
            return productService.getProductsByCategory(category);
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @PostMapping
    public Product createProduct(@RequestBody Product product, Authentication authentication) {
        System.out.println("User " + authentication.getName() + " creating product: " + product.getName());
        return productService.createProduct(product, authentication.getName());
    }

    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody Product product, Authentication authentication) {
        System.out.println("User " + authentication.getName() + " updating product: " + id);
        return productService.updateProduct(id, product, authentication.getName());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id, Authentication authentication) {
        System.out.println("User " + authentication.getName() + " deleting product: " + id);
        productService.deleteProduct(id, authentication.getName());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/categories")
    public List<String> getCategories() {
        return productService.getAllCategories();
    }

    @GetMapping("/my")
    public List<Product> getMyProducts(Authentication authentication) {
        System.out.println("User " + authentication.getName() + " fetching 'my' products. Authorities: "
                + authentication.getAuthorities());
        return productService.getMyProducts(authentication.getName());
    }
}
