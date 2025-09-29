package com.neocart.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.neocart.dto.AddItemToCartRequest;
import com.neocart.dto.CartDto;
import com.neocart.dto.CartItemDto;
import com.neocart.exception.ResourceNotFoundException;
import com.neocart.model.Cart;
import com.neocart.model.CartItem;
import com.neocart.model.Product;
import com.neocart.model.User;
import com.neocart.repository.CartRepository;
import com.neocart.repository.ProductRepository;
import com.neocart.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Transactional
    public CartDto addItemToCart(AddItemToCartRequest request) {
        // 1. Get the currently logged-in user
        User user = getCurrentUser();

        // 2. Find the product to be added
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        // 3. Find the user's cart, or create a new one if it doesn't exist
        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return newCart;
                });

        // 4. Check if the item is already in the cart
        CartItem existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(request.getProductId()))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            // If item exists, just update the quantity
            existingItem.setQuantity(existingItem.getQuantity() + request.getQuantity());
        } else {
            // If item is new, create a new CartItem and add it to the cart
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(request.getQuantity());
            cart.getItems().add(newItem);
        }

        // 5. Save the cart and convert the result to a DTO
        Cart savedCart = cartRepository.save(cart);
        return convertToDto(savedCart);
    }

    public CartDto getCartForCurrentUser() {
        User user = getCurrentUser();
        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });
        return convertToDto(cart);
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        return userRepository.findByEmail(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
    
    private CartDto convertToDto(Cart cart) {
        CartDto cartDto = new CartDto();
        cartDto.setId(cart.getId());
        cartDto.setUserId(cart.getUser().getId());
        cartDto.setItems(
            cart.getItems().stream().map(item -> {
                CartItemDto itemDto = new CartItemDto();
                itemDto.setId(item.getId());
                itemDto.setProductId(item.getProduct().getId());
                itemDto.setProductName(item.getProduct().getName());
                itemDto.setQuantity(item.getQuantity());
                itemDto.setPrice(item.getProduct().getPrice());
                return itemDto;
            }).collect(Collectors.toSet())
        );
        return cartDto;
    }
    @Transactional
    public CartDto updateCartItemQuantity(Long itemId, int quantity) {
        User user = getCurrentUser();
        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user"));

        CartItem itemToUpdate = cart.getItems().stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));

        if (quantity <= 0) {
            // If quantity is 0 or less, remove the item
            cart.getItems().remove(itemToUpdate);
        } else {
            itemToUpdate.setQuantity(quantity);
        }

        Cart savedCart = cartRepository.save(cart);
        return convertToDto(savedCart);
    }

    @Transactional
    public CartDto removeCartItem(Long itemId) {
        User user = getCurrentUser();
        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user"));

        CartItem itemToRemove = cart.getItems().stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));

        cart.getItems().remove(itemToRemove);

        Cart savedCart = cartRepository.save(cart);
        return convertToDto(savedCart);
    } 
}