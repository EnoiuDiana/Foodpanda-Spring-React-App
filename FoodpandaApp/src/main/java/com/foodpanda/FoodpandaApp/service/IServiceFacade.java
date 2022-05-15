package com.foodpanda.FoodpandaApp.service;

import com.foodpanda.FoodpandaApp.dto.MenuItemDTO;
import com.foodpanda.FoodpandaApp.dto.RestaurantDTO;
import com.foodpanda.FoodpandaApp.dto.UserRegisterDTO;
import com.foodpanda.FoodpandaApp.model.*;
import com.itextpdf.text.DocumentException;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
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
     * Method used to create a new admin account
     * @param userRegisterDTO admin details
     */
    public void createAdmin(UserRegisterDTO userRegisterDTO);

    /**
     * Method to create a new restaurant
     * @param restaurantDTO restaurant dto
     * @param admin the logged in admin
     * @throws Exception exception if admin already has a restaurant
     */
    void createNewRestaurant(RestaurantDTO restaurantDTO, Admin admin) throws Exception;

    /**
     * Method to initialize a menu for the restaurant
     * @param restaurant the restaurant that needs the menu
     * @throws Exception if restaurant already has a menu
     */
    void createNewMenu(Restaurant restaurant) throws Exception;

    /**
     * cretae a new menu item for the menu
     * @param menuItemDTO menu item details
     * @param admin the admin that creates the menu item
     * @throws Exception if menu does not exist
     */
    void createNewMenuItem(MenuItemDTO menuItemDTO, Admin admin) throws Exception;

    /**
     * add menu items to a menu
     * @param menuId the menu id
     * @param menuItemId the menu item id
     */
    void addMenuItemsToMenu(Long menuId, Long menuItemId);

    /**
     * get the menu items that an admin has in its restaurant menu
     * @param admin the admin
     * @return a set of menu items
     */
    Set<MenuItem> getMenuItemsForAdmin(Admin admin);

    /**
     * get the menu items for a specific restaurant
     * @param restaurantId the restaurant id
     * @return a set of menu items
     */
    Set<MenuItem> getMenuItemsForRestaurant(Long restaurantId);

    /**
     * get a list with all the restaurants
     * @return a list with restaurants
     */
    List<Restaurant> getRestaurants();

    /**
     * get all the delivery zones
     * @return a list with delivery zones
     */
    List<DeliveryZone> getDeliveryZones();

    /**
     * get the id of the menu that an admin has for its restaurant
     * @param loggedInUser the admin
     * @return the menu id as a Long type
     */
    Long getMenuId(Admin loggedInUser);

    /**
     * adds a menu item to the cart of the customer
     * @param loggedInUser the customer
     * @param menuItemId the menu item id
     * @throws Exception if the menu item that is added belong to a different restaurant
     * than the menu items already added
     */
    void addMenuItemToCart(Customer loggedInUser, Long menuItemId) throws Exception;

    /**
     * place an order by taking the menu items added to a cart for a customer
     * @param loggedInUser the customer
     * @param address the address to deliver the order
     * @param otherDetails the order additional details
     * @throws Exception in case the cart items to order belong to different restaurants
     */
    void placeOrder(Customer loggedInUser, String address, String otherDetails) throws Exception;

    /**
     * get the items that are added in cart for a customer
     * @param loggedInUser the customer
     * @return the a set of items added in cart
     */
    Set<CartItem> getCartItems(Customer loggedInUser);

    /**
     * get all placed orders
     * @param loggedInUser the admin
     * @return list of orders
     */
    List<Order> getAllOrders(Admin loggedInUser);

    /**
     * get the orders placed by a specific customer
     * @param loggedInUser the customer
     * @return list of orders
     */
    List<Order> getOrderForCustomer(Customer loggedInUser);

    /**
     * change the order status from pending to accepted/declined
     * @param orderId order id
     * @param status accepted/declined
     */
    void changeOrderStatus(Long orderId, Status status);

    /**
     * update order status from accepted -> in delivery -> delivered
     * @param orderId order id
     */
    void updateOrderStatus(Long orderId);

    /**
     * export the restaurant menu as pdf
     * @param loggedInUser the admin
     * @return byte array for pdf
     */
    ByteArrayInputStream exportMenuAsPDF(Admin loggedInUser);
}
