package com.neocart.dto;

import lombok.Data;

@Data
public class ProductDto {
    private String name;
    private String description;
    private Double price;
    private Integer stockQuantity;
    private String imageUrl;
    private Long categoryId; // We only need the ID to link the product
}