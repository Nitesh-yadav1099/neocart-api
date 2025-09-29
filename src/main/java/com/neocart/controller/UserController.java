package com.neocart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.neocart.dto.LoginRequest;
import com.neocart.dto.LoginResponse;
import com.neocart.model.User;
import com.neocart.service.UserService;

import jakarta.validation.Valid;

@RestController // Marks this class as a controller that returns JSON data
@RequestMapping("/api/users") // All endpoints in this class will start with /api/users
//@CrossOrigin(origins = "http://localhost:5173") // 2. Add this annotation for getting data from frontend
public class UserController {

    @Autowired
    private UserService userService;

    // Maps HTTP POST requests to this method
    // Full URL: http://localhost:8080/api/users/register
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@Valid @RequestBody User user) {
        User newUser = userService.registerUser(user);
        // Returns the newly created user object and an HTTP status code of 201 (Created)
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }
    
 // Inside UserController.java

 // ... (imports and the registerUser method are above)

 	@PostMapping("/login")
 	public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginRequest loginRequest) {
     	String token = userService.loginUser(loginRequest);
     	return ResponseEntity.ok(new LoginResponse(token));
 	}
 
 	@GetMapping("/profile")
 	public ResponseEntity<String> getUserProfile() {
     	// This is a protected endpoint. Only authenticated users can access it.
     	// Later, we'll get the user from the security context and return their real profile.
    	 return ResponseEntity.ok("Welcome! You have accessed a protected endpoint.");
 	}
}