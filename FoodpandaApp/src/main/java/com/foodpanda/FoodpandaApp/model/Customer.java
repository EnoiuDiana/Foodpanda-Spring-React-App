package com.foodpanda.FoodpandaApp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Entity
/*@DiscriminatorValue(value = "CUSTOMER")*/
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Customer extends User{


    public Customer(String email, String password, String firstName, String lastName) {
        super(email, password, firstName, lastName);
    }

    public Customer(Long id, String email, String password, String firstName, String lastName) {
        super(id, email, password, firstName, lastName);
    }

    @JsonIgnore
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<CartItem> cartItems = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "customerOrder", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Order> orders = new HashSet<>();


}
