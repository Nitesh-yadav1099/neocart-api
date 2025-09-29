package com.neocart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.neocart.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    
}