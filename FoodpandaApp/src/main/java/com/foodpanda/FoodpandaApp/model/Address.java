package com.foodpanda.FoodpandaApp.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "address")
@NoArgsConstructor
@Getter
@Setter
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String street;

    @Column(nullable = false)
    private int streetNo;

    @Column(nullable = false)
    private String city;

    @JsonIgnore
    @OneToOne(mappedBy = "address")
    private Restaurant restaurant;

    public Address(String street, int streetNo, String city) {
        this.street = street;
        this.streetNo = streetNo;
        this.city = city;
    }
}
