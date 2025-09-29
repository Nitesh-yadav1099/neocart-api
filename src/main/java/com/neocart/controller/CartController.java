package com.neocart.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.neocart.dto.AddItemToCartRequest;
import com.neocart.dto.CartDto;
import com.neocart.service.CartService;
import lombok.RequiredArgsConstructor;import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.neocart.dto.UpdateCartItemRequest; // We'll create this DTO


@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<CartDto> getCart() {
        return ResponseEntity.ok(cartService.getCartForCurrentUser());
    }

    @PostMapping("/items")
    public ResponseEntity<CartDto> addItemToCart(@RequestBody AddItemToCartRequest request) {
        return ResponseEntity.ok(cartService.addItemToCart(request));
    }
    @PutMapping("/items/{itemId}")
    public ResponseEntity<CartDto> updateItemQuantity(
            @PathVariable Long itemId, 
            @RequestBody UpdateCartItemRequest request) {
        return ResponseEntity.ok(cartService.updateCartItemQuantity(itemId, request.getQuantity()));
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<CartDto> removeItemFromCart(@PathVariable Long itemId) {
        return ResponseEntity.ok(cartService.removeCartItem(itemId));
    }
}