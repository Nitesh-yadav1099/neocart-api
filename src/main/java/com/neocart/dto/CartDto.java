package com.neocart.dto;

import java.util.Set;
import lombok.Data;

@Data
public class CartDto {
    private Long id;
    private Long userId;
    private Set<CartItemDto> items;
}