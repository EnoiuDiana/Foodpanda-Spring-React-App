package com.foodpanda.FoodpandaApp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "order_item")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name ="order_id", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name ="menuitem_ord_id", nullable = false)
    private MenuItem menuItemOrder;


    public OrderItem(Order order, MenuItem menuItemOrder) {
        this.order = order;
        this.menuItemOrder = menuItemOrder;
    }
}
