package com.neocart.dto;

import lombok.Data;

@Data
public class OrderItemDto {
    private Long id;
    private Long productId;
    private String productName;
    private Integer quantity;
    private Double price;
}