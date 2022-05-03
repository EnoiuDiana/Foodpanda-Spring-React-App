package com.foodpanda.FoodpandaApp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name="delivery_zone")
@NoArgsConstructor
@Getter
@Setter
public class DeliveryZone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique=true)
    private String name;

    @JsonIgnore
    @ManyToMany(mappedBy = "deliveryZones")
    private Collection<Restaurant> restaurants;

    public DeliveryZone(String name) {
        this.name = name;
    }
}
