package com.neocart.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.neocart.dto.CategoryDto;
import com.neocart.dto.ProductDto;
import com.neocart.dto.ProductResponseDto;
import com.neocart.exception.ResourceNotFoundException;
import com.neocart.model.Category;
import com.neocart.model.Product;
import com.neocart.repository.CategoryRepository;
import com.neocart.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    // This method remains the same
    public Product createProduct(ProductDto productDto) {
        Category category = categoryRepository.findById(productDto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + productDto.getCategoryId()));
        Product product = new Product();
        // ... (rest of the create logic is the same)
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setStockQuantity(productDto.getStockQuantity());
        product.setImageUrl(productDto.getImageUrl());
        product.setCategory(category);
        return productRepository.save(product);
    }

    // --- UPDATE THESE METHODS to return DTOs ---

    public List<ProductResponseDto> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public ProductResponseDto getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        return convertToDto(product);
    }

    public List<ProductResponseDto> getProductsByCategoryId(Long categoryId) {
        return productRepository.findByCategoryId(categoryId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // --- Private helper method to convert Entity to DTO ---
    private ProductResponseDto convertToDto(Product product) {
        ProductResponseDto productResponseDto = new ProductResponseDto();
        productResponseDto.setId(product.getId());
        productResponseDto.setName(product.getName());
        productResponseDto.setDescription(product.getDescription());
        productResponseDto.setPrice(product.getPrice());
        productResponseDto.setStockQuantity(product.getStockQuantity());
        productResponseDto.setImageUrl(product.getImageUrl());

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(product.getCategory().getId());
        categoryDto.setName(product.getCategory().getName());
        productResponseDto.setCategory(categoryDto);

        return productResponseDto;
    }
    
    @Transactional
 // 1. Change the return type here
 public ProductResponseDto updateProduct(Long id, ProductDto productDto) { 
     Product product = productRepository.findById(id)
             .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

     Category category = categoryRepository.findById(productDto.getCategoryId())
             .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + productDto.getCategoryId()));

     product.setName(productDto.getName());
     product.setDescription(productDto.getDescription());
     product.setPrice(productDto.getPrice());
     product.setStockQuantity(productDto.getStockQuantity());
     product.setImageUrl(productDto.getImageUrl());
     product.setCategory(category);

     Product savedProduct = productRepository.save(product);
     
     // 2. Convert the saved entity to a DTO before returning
     return convertToDto(savedProduct); 
 }

    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        
        productRepository.delete(product);
    }
}