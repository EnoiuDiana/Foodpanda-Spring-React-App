package com.foodpanda.FoodpandaApp.model;

import lombok.*;

import javax.persistence.*;

@Entity
/*@DiscriminatorValue(value = "ADMIN")*/
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Admin extends User{

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "restaurant_id", referencedColumnName = "id", unique = true)
    private Restaurant restaurant;


}
