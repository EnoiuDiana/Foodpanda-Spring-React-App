package com.foodpanda.FoodpandaApp.service;

import com.foodpanda.FoodpandaApp.model.Customer;
import com.foodpanda.FoodpandaApp.model.MenuItem;
import com.foodpanda.FoodpandaApp.model.OrderItem;
import com.foodpanda.FoodpandaApp.model.Restaurant;
import com.foodpanda.FoodpandaApp.repository.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * service for order items operations
 */
@Service
public class OrderItemService {
    final OrderItemRepository orderItemRepository;

    @Autowired
    public OrderItemService(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    /**
     * add order item
     * @param orderItem order item
     */
    protected void addOrderItem(OrderItem orderItem) {
        orderItemRepository.save(orderItem);
    }
}
