package com.foodpanda.FoodpandaApp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name="restaurant")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id", nullable = false)
    private Address address;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.REMOVE})
    @JoinTable(
            name = "restaurant_delivery_zone",
            joinColumns = @JoinColumn(
                    name = "restaurant_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "delivery_zone_id", referencedColumnName = "id"))
    private Collection<DeliveryZone> deliveryZones;

    @JsonIgnore
    @OneToOne(mappedBy = "restaurant")
    private Menu menu;

    @JsonIgnore
    @OneToMany(mappedBy = "restaurantOrder", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Order> orders = new HashSet<>();

    public Restaurant(String name, Address address, Collection<DeliveryZone> deliveryZones) {
        this.name = name;
        this.address = address;
        this.deliveryZones = deliveryZones;
    }
}
