package com.foodpanda.FoodpandaApp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "cart_item")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name ="customer_id", nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name ="menuitem_id", nullable = false, unique = true)
    private MenuItem menuItem;

    public CartItem(Customer customer, MenuItem menuItem) {
        this.customer = customer;
        this.menuItem = menuItem;
    }

    @PreRemove
    private void preRemove() {
        customer.getCartItems().remove(this);
        menuItem.getCartItems().remove(this);
    }
}
