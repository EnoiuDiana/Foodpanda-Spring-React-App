package com.foodpanda.FoodpandaApp.controller;

import com.foodpanda.FoodpandaApp.dto.UserRegisterDTO;
import com.foodpanda.FoodpandaApp.model.*;
import com.foodpanda.FoodpandaApp.service.IServiceFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    private final IServiceFacade serviceFacade;

    @Autowired
    public CustomerController(IServiceFacade serviceFacade) {
        this.serviceFacade = serviceFacade;
    }

    @PostMapping("/register")
    public ResponseEntity<String> createUser(@RequestBody UserRegisterDTO userRegisterDTO) {
        System.out.println(userRegisterDTO.getEmail());
        serviceFacade.createCustomer(userRegisterDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/viewMenu/{restaurantId}")
    public ResponseEntity<Set<MenuItem>> viewMenu(@PathVariable Long restaurantId) {
        return ResponseEntity.status(HttpStatus.OK).body(serviceFacade.getMenuItemsForRestaurant(restaurantId));
    }

    @GetMapping("/viewRestaurants")
    public ResponseEntity<List<Restaurant>> viewRestaurants() {
        return ResponseEntity.status(HttpStatus.OK).body(serviceFacade.getRestaurants());
    }

    @PostMapping("/addToCart/menuItemId/{menuItemId}")
    public ResponseEntity<String> addToCart(@PathVariable Long menuItemId) {
        User loggedInUser = UserController.loggedInUser;
        try {
            serviceFacade.addMenuItemToCart((Customer) loggedInUser, menuItemId);
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Menu item from another restaurant");
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/placeOrder")
    public ResponseEntity<String> placeOrder() {
        User loggedInUser = UserController.loggedInUser;
        try {
            serviceFacade.placeOrder((Customer) loggedInUser);
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not place order");
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/getCartItems")
    public ResponseEntity<Set<CartItem>> getCartItems() {
        User loggedInUser = UserController.loggedInUser;
        try {
            return ResponseEntity.status(HttpStatus.OK).body(serviceFacade.getCartItems((Customer) loggedInUser));
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/getOrderHistory")
    public ResponseEntity<List<Order>> getOrderHistory() {
        User loggedInUser = UserController.loggedInUser;
        try {
            return ResponseEntity.status(HttpStatus.OK).body(serviceFacade.getOrderForCustomer((Customer) loggedInUser));
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
