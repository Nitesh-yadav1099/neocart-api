package com.neocart.controller;

import com.neocart.dto.OrderDto;
import com.neocart.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderDto> placeOrder() {
        OrderDto newOrder = orderService.placeOrder();
        return new ResponseEntity<>(newOrder, HttpStatus.CREATED);
    }
}