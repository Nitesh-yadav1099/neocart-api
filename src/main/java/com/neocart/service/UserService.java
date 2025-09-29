package com.neocart.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder; // <-- Import this
import org.springframework.stereotype.Service;

import com.neocart.dto.LoginRequest;
import com.neocart.exception.EmailAlreadyExistsException;
import com.neocart.model.Role;
import com.neocart.model.User;
import com.neocart.repository.UserRepository;
import com.neocart.security.JwtService;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // <-- Inject the encoder bean
    
    @Autowired
    private JwtService jwtService; // <-- Inject the JwtService

    public User registerUser(User user) {
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            throw new EmailAlreadyExistsException("Email already in use. Please choose another.");
        }

        // --- HASH THE PASSWORD ---
        // Get the plain-text password from the user object,
        // encode it, and then set it back on the user object.
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        // -------------------------
        
     // --- ADD THIS LOGIC ---
        // If there are no users in the database, make the first one an ADMIN
        if (userRepository.count() == 0) {
            user.setRole(Role.ADMIN);
        } else {
            user.setRole(Role.USER);
        }
        // ---

        return userRepository.save(user);
    }
    // ... (imports and the registerUser method are above)

    public String loginUser(LoginRequest loginRequest) {
        // 1. Find the user by email
        User user = userRepository.findByEmail(loginRequest.getEmail())
            .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + loginRequest.getEmail()));

        // 2. Check if the provided password matches the stored hashed password
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        // 3. If credentials are correct, we'll generate and return a JWT.
     // If credentials are correct, generate and return the JWT
        return jwtService.generateToken(user);
    }
}