package com.foodpanda.FoodpandaApp.repository;

import com.foodpanda.FoodpandaApp.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    Object findByName(String restName);
}
