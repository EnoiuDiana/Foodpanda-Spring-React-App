package com.foodpanda.FoodpandaApp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="menu_item")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
@Builder
public class MenuItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REMOVE})
    @JoinColumn(name="menu_id", nullable=false)
    private Menu menu;

    @Column( nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private float price;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Category category;

    @JsonIgnore
    @OneToMany(mappedBy = "menuItem", cascade = CascadeType.ALL)
    private Set<CartItem> cartItems = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "menuItemOrder", cascade = CascadeType.ALL)
    private Set<OrderItem> orderItems = new HashSet<>();

    public MenuItem(Menu menu, String name, String description, float price, Category category) {
        this.menu = menu;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
    }
}
