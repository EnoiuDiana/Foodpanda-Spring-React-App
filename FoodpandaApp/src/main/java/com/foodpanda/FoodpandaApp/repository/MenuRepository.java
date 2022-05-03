package com.foodpanda.FoodpandaApp.repository;

import com.foodpanda.FoodpandaApp.model.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
    Menu findByRestaurantId(Long id);
}
