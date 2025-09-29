package com.neocart.model;

import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
//import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "carts")
@Getter // Use Getter
@Setter // Use Setter
//@Data
public class Cart {

    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId // This makes the Cart's ID the same as the User's ID
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude // Exclude this field from the toString() method
    private Set<CartItem> items = new HashSet<>();

    // We also need to manually override equals() and hashCode() to avoid the loop
    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cart cart = (Cart) o;
        return getId() != null && getId().equals(cart.getId());
    }

    @Override
    public final int hashCode() {
        return getClass().hashCode();
    }
}