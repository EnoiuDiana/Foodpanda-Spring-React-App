package com.foodpanda.FoodpandaApp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "placed_order")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name ="customer_ord_id", nullable = false)
    private Customer customerOrder;

    @ManyToOne
    @JoinColumn(name ="restaurant_ord_id", nullable = false)
    private Restaurant restaurantOrder;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private Set<OrderItem> orderItems = new HashSet<>();

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Status status;

    public Order(Customer customerOrder, Restaurant restaurantOrder) {
        this.customerOrder = customerOrder;
        this.restaurantOrder = restaurantOrder;
        this.status = Status.PENDING;
    }
}
