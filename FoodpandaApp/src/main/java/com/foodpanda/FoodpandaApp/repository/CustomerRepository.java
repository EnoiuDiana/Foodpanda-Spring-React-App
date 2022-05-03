package com.foodpanda.FoodpandaApp.repository;

import com.foodpanda.FoodpandaApp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<User, Long> {
}
