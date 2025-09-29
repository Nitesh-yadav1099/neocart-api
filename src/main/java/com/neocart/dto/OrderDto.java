package com.neocart.dto;

import com.neocart.model.OrderStatus;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDto {
    private Long id;
    private Long userId;
    private LocalDateTime orderDate;
    private OrderStatus status;
    private Double totalAmount;
    private List<OrderItemDto> orderItems;
}