package com.foodpanda.FoodpandaApp.repository;

import com.foodpanda.FoodpandaApp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByEmailAndPassword(String email, String password);
    User findUserByEmail(String email);
    User findUserById(Long Id);
}
