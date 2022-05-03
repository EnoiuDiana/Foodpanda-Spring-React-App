package com.foodpanda.FoodpandaApp.service;

import com.foodpanda.FoodpandaApp.model.Customer;
import com.foodpanda.FoodpandaApp.model.Order;
import com.foodpanda.FoodpandaApp.model.Status;
import com.foodpanda.FoodpandaApp.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
public class OrderService {

    final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    protected void addOrder(Order order) {
        orderRepository.save(order);
    }

    protected List<Order> findAllOrders() {
        return orderRepository.findAll();
    }

    protected List<Order> findAllOrdersForCustomer(Customer customer) {
        return orderRepository.findAllByCustomerOrderId(customer.getId());
    }

    protected void changeStatus(long orderId, Status status) {
        Order order = orderRepository.getById(orderId);
        order.setStatus(status);
        orderRepository.save(order);
    }
    protected void updateStatus(long orderId) {
        Order order = orderRepository.getById(orderId);
        if(order.getStatus() == Status.ACCEPTED) {
            order.setStatus(Status.IN_DELIVERY);
        }
        else if(order.getStatus() == Status.IN_DELIVERY) {
            order.setStatus(Status.DELIVERED);
        }
        orderRepository.save(order);
    }
}
