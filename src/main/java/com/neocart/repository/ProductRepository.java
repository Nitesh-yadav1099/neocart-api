package com.neocart.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.neocart.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    // Spring Data JPA will automatically create the query for us from the method name
    List<Product> findByCategoryId(Long categoryId);    
}