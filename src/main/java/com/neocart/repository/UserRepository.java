package com.neocart.repository;

import java.util.Optional; // Make sure to import Optional
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.neocart.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Spring Data JPA will automatically generate a query from this method name.
     * It will look for a user with the matching email address.
     * Using Optional is a good practice to handle cases where the user might not be found.
     */
    Optional<User> findByEmail(String email);
}