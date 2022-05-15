package com.foodpanda.FoodpandaApp.controller;

import com.foodpanda.FoodpandaApp.dto.OrderDetailsDTO;
import com.foodpanda.FoodpandaApp.dto.UserRegisterDTO;
import com.foodpanda.FoodpandaApp.model.*;
import com.foodpanda.FoodpandaApp.security.services.UserDetailsImpl;
import com.foodpanda.FoodpandaApp.service.IServiceFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    private final IServiceFacade serviceFacade;

    private static final Logger logger = LoggerFactory.
            getLogger(CustomerController.class);

    @Autowired
    public CustomerController(IServiceFacade serviceFacade) {
        this.serviceFacade = serviceFacade;
    }

    @PostMapping("/register")
    public ResponseEntity<String> createUser(@RequestBody UserRegisterDTO userRegisterDTO) {
        logger.info(String.format("Create a new user for email %s", userRegisterDTO.getEmail()));
        System.out.println(userRegisterDTO.getEmail());
        serviceFacade.createCustomer(userRegisterDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/viewMenu/{restaurantId}")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<Set<MenuItem>> viewMenu(@PathVariable Long restaurantId) {
        logger.info(String.format("View menu for restaurant id %d", restaurantId));
        return ResponseEntity.status(HttpStatus.OK).body(serviceFacade.getMenuItemsForRestaurant(restaurantId));
    }

    @GetMapping("/viewRestaurants")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<List<Restaurant>> viewRestaurants() {
        logger.info("View all restaurants");
        return ResponseEntity.status(HttpStatus.OK).body(serviceFacade.getRestaurants());
    }

    @PostMapping("/addToCart/menuItemId/{menuItemId}")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<String> addToCart(@PathVariable Long menuItemId) {
        logger.info(String.format("Add to cart the menu item with id %d", menuItemId));
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User loggedInUser = userDetails.getUser();
        //User loggedInUser = UserController.loggedInUser;
        try {
            serviceFacade.addMenuItemToCart((Customer) loggedInUser, menuItemId);
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Menu item from another restaurant");
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/placeOrder")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<String> placeOrder(@RequestBody OrderDetailsDTO orderDetailsDTO) {
        logger.info("Place a new order");
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User loggedInUser = userDetails.getUser();
        //User loggedInUser = UserController.loggedInUser;
        try {
            serviceFacade.placeOrder((Customer) loggedInUser, orderDetailsDTO.getAddress(), orderDetailsDTO.getOtherDetails());
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not place order");
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/getCartItems")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<Set<CartItem>> getCartItems() {
        logger.info("Retrieving all cart items");
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User loggedInUser = userDetails.getUser();
        //User loggedInUser = UserController.loggedInUser;
        try {
            return ResponseEntity.status(HttpStatus.OK).body(serviceFacade.getCartItems((Customer) loggedInUser));
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/getOrderHistory")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<List<Order>> getOrderHistory() {
        logger.info("Getting all the order history");
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User loggedInUser = userDetails.getUser();
        //User loggedInUser = UserController.loggedInUser;
        try {
            return ResponseEntity.status(HttpStatus.OK).body(serviceFacade.getOrderForCustomer((Customer) loggedInUser));
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
