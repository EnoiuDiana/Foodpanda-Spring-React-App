package com.foodpanda.FoodpandaApp.service;

import com.foodpanda.FoodpandaApp.model.CartItem;
import com.foodpanda.FoodpandaApp.model.Customer;

import com.foodpanda.FoodpandaApp.model.MenuItem;
import com.foodpanda.FoodpandaApp.repository.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
public class CartItemService {

    final CartItemRepository cartItemRepository;

    @Autowired
    public CartItemService(CartItemRepository cartItemRepository) {
        this.cartItemRepository = cartItemRepository;
    }

    protected void addNewCartItem(Customer customer, MenuItem menuItem) {
        CartItem cartItem = new CartItem(customer, menuItem);
        Set<CartItem> cartItems = customer.getCartItems();
        cartItems.add(cartItem);
        customer.setCartItems(cartItems);
        cartItemRepository.save(cartItem);
    }

    @Transactional
    protected void deleteAllCartItems(Customer customer) {
        cartItemRepository.deleteAllByCustomer(customer);
    }

    protected Set<CartItem> getCartItems(Customer customer) {
        return cartItemRepository.findAllByCustomer(customer);
    }
}
