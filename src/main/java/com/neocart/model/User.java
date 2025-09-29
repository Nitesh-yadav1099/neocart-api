package com.neocart.model;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Represents a user in the application.
 * This class is a JPA entity that maps to the "users" table in the database.
 */
@Data // Lombok: Generates getters, setters, toString(), equals(), and hashCode()
@NoArgsConstructor // Lombok: Generates a no-argument constructor
@AllArgsConstructor // Lombok: Generates a constructor with all arguments
@Entity // JPA: Marks this class as a JPA entity
@Table(name = "users") // JPA: Specifies the table name in the database
public class User implements UserDetails {

    @Id // JPA: Marks this field as the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // JPA: Configures the primary key generation strategy to auto-increment
    private Long id;

    @NotBlank(message = "First name is required")
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Password is required")
    @Column(nullable = false)
    private String password;
    
    @Enumerated(EnumType.STRING) // Stores the enum as a string ("USER", "ADMIN")
    private Role role;

    // We can add more fields later, like address, phone number, roles, etc.

    @CreationTimestamp // Hibernate: Automatically sets the value when the entity is first created
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp // Hibernate: Automatically sets the value when the entity is updated
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
 // Add these methods at the bottom of the class
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        // We will handle roles later. For now, return an empty list.
//        return List.of();
//    }
    
 // --- REPLACE the old getAuthorities() method with this ---
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" +role.name()));
    }
    
    @Override
    public String getUsername() {
        // Our "username" is the email address
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }



    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}