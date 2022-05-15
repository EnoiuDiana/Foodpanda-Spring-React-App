package com.foodpanda.FoodpandaApp.service;

import com.foodpanda.FoodpandaApp.controller.UserController;
import com.foodpanda.FoodpandaApp.dto.MenuItemDTO;
import com.foodpanda.FoodpandaApp.dto.RestaurantDTO;
import com.foodpanda.FoodpandaApp.dto.UserRegisterDTO;
import com.foodpanda.FoodpandaApp.model.*;
import com.foodpanda.FoodpandaApp.service.MailSender.EmailServiceImpl;
import com.foodpanda.FoodpandaApp.service.MenuPdfGenerator.GeneratePDFMenu;
import com.itextpdf.text.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The implementation for the ServiceFacade interface. It mediates between all the services that we have
 */
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
    public ServiceMediatorImpl(RestaurantService restaurantService,
                               CustomerService customerService,
                               AdminService adminService,
                               MenuService menuService,
                               MenuItemService menuItemService,
                               DeliveryZoneService deliveryZoneService,
                               CartItemService cartItemService,
                               OrderItemService orderItemService,
                               OrderService orderService) {
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

    @Autowired
    private EmailServiceImpl emailService;

    private static final Logger logger = LoggerFactory.getLogger(ServiceMediatorImpl.class);

    /**
     * @inheritDoc
     * @param userRegisterDTO data transfer object user register
     */
    public void createCustomer(UserRegisterDTO userRegisterDTO) {
        logger.info("Creating a new customer account");
        customerService.createCustomer(userRegisterDTO);
    }

    /**
     * @inheritDoc
     * @param userRegisterDTO admin details
     */
    public void createAdmin(UserRegisterDTO userRegisterDTO) {
        logger.info("Creating a new admin account");
        adminService.createAdmin(userRegisterDTO);
    }

    /**
     * @inheritDoc
     * @param restaurantDTO restaurant dto
     * @param admin the logged in admin
     * @throws Exception
     */
    public void createNewRestaurant(RestaurantDTO restaurantDTO, Admin admin) throws Exception {
        logger.info("Creating a new restaurant");
        Restaurant restaurant = restaurantService.createRestaurant(restaurantDTO, admin);
        adminService.setRestaurant(admin, restaurant);
    }

    /**
     * @inheritDoc
     * @param restaurant the restaurant that needs the menu
     * @throws Exception
     */
    public void createNewMenu(Restaurant restaurant) throws Exception {
        logger.info("Creating a new menu for restaurant");
        menuService.createMenu(restaurant);
    }

    /**
     * @inheritDoc
     * @param menuItemDTO menu item details
     * @param admin the admin that creates the menu item
     * @throws Exception
     */
    public void createNewMenuItem(MenuItemDTO menuItemDTO, Admin admin) throws Exception {
        logger.info("Creating a new menu item");
        menuItemService.createMenuItem(menuItemDTO, admin);
    }


    /**
     * @inheritDoc
     * @param menuId the menu id
     * @param menuItemId the menu item id
     */
    public void addMenuItemsToMenu(Long menuId, Long menuItemId) {
        logger.info("Adding menu items to menu");
        Menu menu = menuService.findMenuById(menuId);
        MenuItem menuItem = menuItemService.findMenuItemById(menuItemId);
        Set<MenuItem> menuItems = menu.getMenuItems();
        menuItems.add(menuItem);
        menu.setMenuItems(menuItems);
        menuService.updateMenu(menu);
    }

    /**
     * @inheritDoc
     * @param admin the admin
     * @return
     */
    public Set<MenuItem> getMenuItemsForAdmin(Admin admin){
        logger.info("getting all menu items for an admin");
        Restaurant restaurant = admin.getRestaurant();
        if(restaurant != null) {
            Menu menu = restaurant.getMenu();
            if (menu != null) {
                return menu.getMenuItems();
            }
        }
        return null;
    }

    /**
     * @inheritDoc
     * @param restaurantId the restaurant id
     * @return
     */
    public Set<MenuItem> getMenuItemsForRestaurant(Long restaurantId) {
        logger.info("Getting all menu items for a restaurant");
        Restaurant restaurant = restaurantService.findRestaurantById(restaurantId);
        Menu menu = restaurant.getMenu();
        if(menu != null) {
            return menu.getMenuItems();
        }
        return null;
    }

    /**
     * @inheritDoc
     * @return
     */
    public List<Restaurant> getRestaurants() {
        logger.info("Getting all restaurants");
        return restaurantService.findAllRestaurants();
    }

    /**
     * @inheritDoc
     * @return
     */
    public List<DeliveryZone> getDeliveryZones() {
        logger.info("Getting all delivery zones");
        return deliveryZoneService.findAll();
    }

    /**
     * @inheritDoc
     * @param loggedInUser the admin
     * @return
     */
    public Long getMenuId(Admin loggedInUser) {
        logger.info("Getting all menu items");
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

    /**
     * @inheritDoc
     * @param loggedInUser the customer
     * @param menuItemId the menu item id
     * @throws Exception
     */
    public void addMenuItemToCart(Customer loggedInUser, Long menuItemId) throws Exception {
        logger.info("adding a menu item to cart");
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

    /**
     * @inheritDoc
     * @param loggedInUser the customer
     * @param address the address to deliver the order
     * @param otherDetails the order additional details
     * @throws Exception
     */
    @Transactional
    public void placeOrder(Customer loggedInUser, String address, String otherDetails) throws Exception {
        logger.info("Placing an order");
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

        List<OrderItem> allOrderedItems = new ArrayList<>();
        for(CartItem cartItem : cartItems) {
            MenuItem menuItem = menuItemService.findMenuItemById(cartItem.getMenuItem().getId());
            OrderItem orderItem = new OrderItem(order, menuItem);
            orderItemService.addOrderItem(orderItem);
            allOrderedItems.add(orderItem);
        }
        loggedInUser.getCartItems().removeAll(cartItems);
        cartItemService.deleteAllCartItems(loggedInUser);

        emailService.sendSimpleMessage("edianagte@yahoo.com",
                "Order details",
                emailService.buildOrderDetailsEmail(loggedInUser,
                        allOrderedItems,
                        address,
                        otherDetails));
    }

    /**
     * @inheritDoc
     * @param loggedInUser the customer
     * @return
     */
    public Set<CartItem> getCartItems(Customer loggedInUser) {
        logger.info("Getting all cart items");
        return cartItemService.getCartItems(loggedInUser);
    }

    /**
     * @inheritDoc
     * @param loggedInUser the admin
     * @return
     */
    public List<Order> getAllOrders(Admin loggedInUser) {
        logger.info("Getting all orders");
        Restaurant restaurant = loggedInUser.getRestaurant();

        List<Order> allOrders = orderService.findAllOrders();
        return  allOrders.stream().filter((order -> order.getRestaurantOrder().getId().equals(restaurant.getId()))).collect(Collectors.toList());
    }

    /**
     * @inheritDoc
     * @param loggedInUser the customer
     * @return
     */
    public List<Order> getOrderForCustomer(Customer loggedInUser) {
        logger.info("Getting all orders for customer");
        return orderService.findAllOrdersForCustomer(loggedInUser);
    }

    /**
     * @inheritDoc
     * @param orderId order id
     * @param status accepted/declined
     */
    public void changeOrderStatus(Long orderId, Status status) {
        logger.info("Update the order status");
        orderService.changeStatus(orderId, status);
    }

    /**
     * @inheritDoc
     * @param orderId order id
     */
    public void updateOrderStatus(Long orderId) {
        logger.info("Update the order status");
        orderService.updateStatus(orderId);
    }

    /**
     * @inheritDoc
     * @param loggedInUser the admin
     * @return
     */
    @Override
    public ByteArrayInputStream exportMenuAsPDF(Admin loggedInUser) {
        logger.info("Exporting menu as a pdf");
        Restaurant restaurant = loggedInUser.getRestaurant();
        if(restaurant != null) {
            Menu menu = restaurant.getMenu();
            if (menu != null) {
                 Set<MenuItem> menuItems = menu.getMenuItems();
                GeneratePDFMenu generatePDFMenu = new GeneratePDFMenu();
                return generatePDFMenu.generateMenuPdf(menuItems);
            }
        }
        return null;
    }
}
