package com.neocart.dto;

import lombok.Data;

@Data
public class ProductResponseDto {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Integer stockQuantity;
    private String imageUrl;
    private CategoryDto category; // Embed the CategoryDto
}