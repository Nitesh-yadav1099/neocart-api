package com.neocart.repository;

import com.neocart.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    // We can add methods to find orders by user later
}