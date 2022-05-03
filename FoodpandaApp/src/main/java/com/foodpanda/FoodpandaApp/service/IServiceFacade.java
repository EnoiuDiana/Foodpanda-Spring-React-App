package com.foodpanda.FoodpandaApp.service;

import com.foodpanda.FoodpandaApp.dto.MenuItemDTO;
import com.foodpanda.FoodpandaApp.dto.RestaurantDTO;
import com.foodpanda.FoodpandaApp.dto.UserRegisterDTO;
import com.foodpanda.FoodpandaApp.model.*;

import java.util.List;
import java.util.Set;

/**
 * Interface service with whom the controllers communicate with
 */
public interface IServiceFacade {

    /**
     * Method to register a new customer
     * @param userRegisterDTO data transfer object user register
     */
    void createCustomer(UserRegisterDTO userRegisterDTO);

    /**
     * Method to create a new restaurant
     * @param restaurantDTO restaurant dto
     * @param admin the logged in admin
     * @throws Exception exception if admin already has a restaurant
     */
    void createNewRestaurant(RestaurantDTO restaurantDTO, Admin admin) throws Exception;
    void createNewMenu(Restaurant restaurant) throws Exception;
    void createNewMenuItem(MenuItemDTO menuItemDTO, Admin admin) throws Exception;
    void addMenuItemsToMenu(Long menuId, Long menuItemId);
    Set<MenuItem> getMenuItemsForAdmin(Admin admin);
    Set<MenuItem> getMenuItemsForRestaurant(Long restaurantId);
    List<Restaurant> getRestaurants();
    List<DeliveryZone> getDeliveryZones();
    Long getMenuId(Admin loggedInUser);
    void addMenuItemToCart(Customer loggedInUser, Long menuItemId) throws Exception;
    void placeOrder(Customer loggedInUser) throws Exception;
    Set<CartItem> getCartItems(Customer loggedInUser);
    List<Order> getAllOrders(Admin loggedInUser);
    List<Order> getOrderForCustomer(Customer loggedInUser);
    void changeOrderStatus(Long orderId, Status status);
    void updateOrderStatus(Long orderId);

}
