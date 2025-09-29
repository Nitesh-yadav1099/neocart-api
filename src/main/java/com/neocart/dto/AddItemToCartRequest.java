package com.neocart.dto;

import lombok.Data;

@Data
public class AddItemToCartRequest {
    private Long productId;
    private int quantity;
}										