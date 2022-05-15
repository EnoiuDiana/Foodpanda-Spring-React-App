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

/**
 * service for cart items operations
 */
@Service
public class CartItemService {

    final CartItemRepository cartItemRepository;

    @Autowired
    public CartItemService(CartItemRepository cartItemRepository) {
        this.cartItemRepository = cartItemRepository;
    }

    /**
     * add a new cart item for a customer
     * @param customer the customer
     * @param menuItem new cart item
     */
    protected void addNewCartItem(Customer customer, MenuItem menuItem) {
        CartItem cartItem = new CartItem(customer, menuItem);
        Set<CartItem> cartItems = customer.getCartItems();
        cartItems.add(cartItem);
        customer.setCartItems(cartItems);
        cartItemRepository.save(cartItem);
    }

    /**
     * used to delete cart items
     * @param customer the customer
     */
    @Transactional
    protected void deleteAllCartItems(Customer customer) {
        cartItemRepository.deleteAllByCustomer(customer);
    }

    /**
     * retrieve all cart items for a customer
     * @param customer the customer
     * @return set of cart items
     */
    protected Set<CartItem> getCartItems(Customer customer) {
        return cartItemRepository.findAllByCustomer(customer);
    }
}
