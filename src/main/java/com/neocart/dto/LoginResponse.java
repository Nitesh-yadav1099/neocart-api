package com.neocart.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor // A constructor for the token
public class LoginResponse {
    private String token;
}