package com.foodpanda.FoodpandaApp.repository;

import com.foodpanda.FoodpandaApp.model.CartItem;
import com.foodpanda.FoodpandaApp.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    void deleteAllByCustomerId(Long customerId);
    @Transactional
    void deleteAllByCustomer(Customer customer);

    Set<CartItem> findAllByCustomer(Customer customer);
}
