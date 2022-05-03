package com.foodpanda.FoodpandaApp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="menu")
@NoArgsConstructor
@Getter
@Setter
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.REMOVE})
    @JoinColumn(name = "restaurant_id", referencedColumnName = "id", unique = true, nullable = false)
    private Restaurant restaurant;

    @JsonIgnore
    @OneToMany(mappedBy="menu", fetch = FetchType.EAGER)
    private Set<MenuItem> menuItems;

    public Menu(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
}
