package com.neocart.service;

import com.neocart.exception.ResourceNotFoundException;
import com.neocart.model.*;
import com.neocart.repository.CartRepository;
import com.neocart.repository.OrderRepository;
import com.neocart.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.neocart.dto.OrderDto;
import com.neocart.dto.OrderItemDto;

import java.util.stream.Collectors;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;

    @Transactional
    public OrderDto placeOrder() {
        // 1. Get the current user
        User user = getCurrentUser();

        // 2. Find the user's cart
        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user"));

        if (cart.getItems().isEmpty()) {
            throw new IllegalStateException("Cannot place an order with an empty cart.");
        }

        // 3. Create a new Order
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);

        // 4. Convert CartItems to OrderItems
        double totalAmount = 0.0;
        for (CartItem cartItem : cart.getItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getProduct().getPrice()); // Record price at time of purchase
            
            order.getOrderItems().add(orderItem);
            totalAmount += orderItem.getPrice() * orderItem.getQuantity();
        }

        order.setTotalAmount(totalAmount);

        // 5. Clear the user's cart
        cart.getItems().clear();
        cartRepository.save(cart);
        Order savedOrder = orderRepository.save(order);
        // 6. Save the new order (OrderItems are saved automatically due to CascadeType.ALL)
        return convertToDto(savedOrder);
    }
    
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        return userRepository.findByEmail(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
    private OrderDto convertToDto(Order order) {
        OrderDto orderDto = new OrderDto();
        orderDto.setId(order.getId());
        orderDto.setUserId(order.getUser().getId());
        orderDto.setOrderDate(order.getOrderDate());
        orderDto.setStatus(order.getStatus());
        orderDto.setTotalAmount(order.getTotalAmount());
        
        List<OrderItemDto> itemDtos = order.getOrderItems().stream().map(item -> {
            OrderItemDto itemDto = new OrderItemDto();
            itemDto.setId(item.getId());
            itemDto.setProductId(item.getProduct().getId());
            itemDto.setProductName(item.getProduct().getName());
            itemDto.setQuantity(item.getQuantity());
            itemDto.setPrice(item.getPrice());
            return itemDto;
        }).collect(Collectors.toList());
        
        orderDto.setOrderItems(itemDtos);
        
        return orderDto;
    }
}