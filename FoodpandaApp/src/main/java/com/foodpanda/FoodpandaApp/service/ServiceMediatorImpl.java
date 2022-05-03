package com.foodpanda.FoodpandaApp.service;

import com.foodpanda.FoodpandaApp.dto.MenuItemDTO;
import com.foodpanda.FoodpandaApp.dto.RestaurantDTO;
import com.foodpanda.FoodpandaApp.dto.UserRegisterDTO;
import com.foodpanda.FoodpandaApp.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ServiceMediatorImpl implements IServiceFacade{

    private final RestaurantService restaurantService;
    private final CustomerService customerService;
    private final AdminService adminService;
    private final MenuService menuService;
    private final MenuItemService menuItemService;
    private final DeliveryZoneService deliveryZoneService;
    private final CartItemService cartItemService;
    private final OrderItemService orderItemService;
    private final OrderService orderService;

    @Autowired
    public ServiceMediatorImpl(RestaurantService restaurantService, CustomerService customerService, AdminService adminService, MenuService menuService, MenuItemService menuItemService, DeliveryZoneService deliveryZoneService, CartItemService cartItemService, OrderItemService orderItemService, OrderService orderService) {
        this.restaurantService = restaurantService;
        this.customerService = customerService;
        this.adminService = adminService;
        this.menuService = menuService;
        this.menuItemService = menuItemService;
        this.deliveryZoneService = deliveryZoneService;
        this.cartItemService = cartItemService;
        this.orderItemService = orderItemService;
        this.orderService = orderService;
    }

    public void createCustomer(UserRegisterDTO userRegisterDTO) {
        customerService.createCustomer(userRegisterDTO);
    }

    public void createNewRestaurant(RestaurantDTO restaurantDTO, Admin admin) throws Exception {
        Restaurant restaurant = restaurantService.createRestaurant(restaurantDTO, admin);
        adminService.setRestaurant(admin, restaurant);
    }

    public void createNewMenu(Restaurant restaurant) throws Exception {
        menuService.createMenu(restaurant);
    }

    public void createNewMenuItem(MenuItemDTO menuItemDTO, Admin admin) throws Exception {
        menuItemService.createMenuItem(menuItemDTO, admin);
    }

    public void addMenuItemsToMenu(Long menuId, Long menuItemId) {
        Menu menu = menuService.findMenuById(menuId);
        MenuItem menuItem = menuItemService.findMenuItemById(menuItemId);
        Set<MenuItem> menuItems = menu.getMenuItems();
        menuItems.add(menuItem);
        menu.setMenuItems(menuItems);
        menuService.updateMenu(menu);
    }

    public Set<MenuItem> getMenuItemsForAdmin(Admin admin){
        Restaurant restaurant = admin.getRestaurant();
        if(restaurant != null) {
            Menu menu = restaurant.getMenu();
            if (menu != null) {
                return menu.getMenuItems();
            }
        }
        return null;
    }

    public Set<MenuItem> getMenuItemsForRestaurant(Long restaurantId) {
        Restaurant restaurant = restaurantService.findRestaurantById(restaurantId);
        Menu menu = restaurant.getMenu();
        if(menu != null) {
            return menu.getMenuItems();
        }
        return null;
    }

    public List<Restaurant> getRestaurants() {
        return restaurantService.findAllRestaurants();
    }

    public List<DeliveryZone> getDeliveryZones() {
        return deliveryZoneService.findAll();
    }

    public Long getMenuId(Admin loggedInUser) {
        Restaurant restaurant = loggedInUser.getRestaurant();
        if(restaurant == null) {
            return null;
        }
        Menu menu = restaurant.getMenu();
        if(menu == null) {
            return null;
        }
        return menu.getId();
    }

    public void addMenuItemToCart(Customer loggedInUser, Long menuItemId) throws Exception {
        Set<CartItem> cartItems = loggedInUser.getCartItems();
        MenuItem menuItem = menuItemService.findMenuItemById(menuItemId);
        if(cartItems.size() > 0) {
            for (CartItem cartItem : cartItems) {
                if (cartItem.getMenuItem().getId().equals(menuItemId)) {
                    return; //menu item already in cart
                }
            }
            Long restaurantId = cartItems.iterator().next().getMenuItem().getMenu().getRestaurant().getId();
            if (!restaurantId.equals(menuItem.getMenu().getRestaurant().getId())) {
                throw new Exception("Menu item from another restaurant");
            }
        }
        cartItemService.addNewCartItem(loggedInUser, menuItem);
    }

    @Transactional
    public void placeOrder(Customer loggedInUser) throws Exception {
        Set<CartItem> cartItems = loggedInUser.getCartItems();
        Set<Long> restaurantsId = cartItems.stream()
                .map(cartItem -> cartItem.getMenuItem().getMenu().getRestaurant().getId())
                .collect(Collectors.toSet());

        if (restaurantsId.size() > 1) {
            throw new Exception("Order from multiple restaurants");
        }

        Restaurant restaurant = restaurantService.findRestaurantById(restaurantsId.iterator().next());
        Order order = new Order(loggedInUser, restaurant);
        orderService.addOrder(order);

        for(CartItem cartItem : cartItems) {
            MenuItem menuItem = menuItemService.findMenuItemById(cartItem.getMenuItem().getId());
            OrderItem orderItem = new OrderItem(order, menuItem);
            orderItemService.addOrderItem(orderItem);
        }
        loggedInUser.getCartItems().removeAll(cartItems);
        cartItemService.deleteAllCartItems(loggedInUser);
    }

    public Set<CartItem> getCartItems(Customer loggedInUser) {
        return cartItemService.getCartItems(loggedInUser);
    }

    public List<Order> getAllOrders(Admin loggedInUser) {
        Restaurant restaurant = loggedInUser.getRestaurant();

        List<Order> allOrders = orderService.findAllOrders();
        return  allOrders.stream().filter((order -> order.getRestaurantOrder().getId().equals(restaurant.getId()))).collect(Collectors.toList());
    }

    public List<Order> getOrderForCustomer(Customer loggedInUser) {
        return orderService.findAllOrdersForCustomer(loggedInUser);
    }

    public void changeOrderStatus(Long orderId, Status status) {
        orderService.changeStatus(orderId, status);
    }

    public void updateOrderStatus(Long orderId) {
        orderService.updateStatus(orderId);
    }
}
