package com.foodpanda.FoodpandaApp.service;

import com.foodpanda.FoodpandaApp.FoodpandaAppApplication;
import com.foodpanda.FoodpandaApp.dto.RestaurantDTO;
import com.foodpanda.FoodpandaApp.dto.UserRegisterDTO;
import com.foodpanda.FoodpandaApp.model.*;
import com.foodpanda.FoodpandaApp.repository.*;
import com.foodpanda.FoodpandaApp.security.services.UserDetailsImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = FoodpandaAppApplication.class)
public class AddToCartTest {

    @Autowired
    private ServiceMediatorImpl serviceMediatorImpl;

    @MockBean
    private RestaurantService restaurantService;

    @MockBean
    private CustomerService customerService;

    @MockBean
    private AdminService adminService;

    @MockBean
    private MenuService menuService;

    @MockBean
    private MenuItemService menuItemService;

    @MockBean
    private DeliveryZoneService deliveryZoneService;

    @MockBean
    private CartItemService cartItemService;

    @MockBean
    private OrderItemService orderItemService;

    @MockBean
    private OrderService orderService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private AdminRepository adminRepository;

    @MockBean
    private RestaurantRepository restaurantRepository;

    @MockBean
    private MenuItemRepository menuItemRepository;

    @MockBean
    private CartItemRepository cartItemRepository;

    private final Admin newAdmin = new Admin("abbbaa@gmail.com", "pass1234", "Test", "Admin");
    private final UserRegisterDTO userRegisterDTO = new UserRegisterDTO("abbbaa@gmail.com", "pass1234", "Test", "Admin");
    private final Address address = new Address("Street", 3, "Cluj");
    private final RestaurantDTO restaurantDTO = new RestaurantDTO("Test restaurant", address, new ArrayList<>());
    private final Restaurant restaurant = new Restaurant(1L, "Test restaurant", address, new ArrayList<>(), null, new HashSet<>());
    private final Restaurant restaurant2 = new Restaurant(2L, "Test restaurant2", address, new ArrayList<>(), null, new HashSet<>());
    private final Menu menu = new Menu(1L, restaurant, new HashSet<>());
    private final Menu menu2 = new Menu(2L, restaurant2, new HashSet<>());
    private final Customer newCustomer = new Customer(1L, "testcustomer@gmail.com", "pass1234", "Test", "Customer");

    private final MenuItem menuItem1 = new MenuItem(1L, menu, "food1", "aa", 12.5F, Category.DESSERT, new HashSet<>(), new HashSet<>());
    private final CartItem cartItem1= new CartItem(newCustomer, menuItem1);
    private final MenuItem menuItem2 = new MenuItem(2L, menu, "food2", "aa", 12.5F, Category.DESSERT, new HashSet<>(), new HashSet<>());
    private final CartItem cartItem2= new CartItem(newCustomer, menuItem2);
    private final MenuItem menuItem3 = new MenuItem(3L, menu, "food3", "aa", 12.5F, Category.DESSERT, new HashSet<>(), new HashSet<>());

    private final MenuItem menuItem4 = new MenuItem(4L, menu2, "food4", "aa", 12.5F, Category.DESSERT, new HashSet<>(), new HashSet<>());

    @Before
    public void setup() {

        //set menus to restaurants
        restaurant.setMenu(menu);
        restaurant2.setMenu(menu2);


        //add menu items
        menuItemRepository.save(menuItem1);
        menuItemRepository.save(menuItem2);
        menuItemRepository.save(menuItem3);
        menuItemRepository.save(menuItem4);
        cartItemRepository.save(cartItem1);
        cartItemRepository.save(cartItem2);

        Mockito.when(menuItemService.findMenuItemById(menuItem3.getId())).thenReturn(menuItem3);
        Mockito.when(menuItemService.findMenuItemById(menuItem4.getId())).thenReturn(menuItem4);

        Mockito.mock(CartItemService.class);

        Set<CartItem> oldCartItems = new HashSet<>();
        oldCartItems.add(cartItem1);
        oldCartItems.add(cartItem2);
        newCustomer.setCartItems(oldCartItems);

        Mockito.when(cartItemService.getCartItems(newCustomer)).thenReturn(oldCartItems);
        assertEquals(2, newCustomer.getCartItems().size());
        Mockito.when(restaurantRepository.findByName("Test restaurant")).thenReturn(java.util.Optional.of(restaurant));
        Mockito.when(menuItemRepository.findByName("food3")).thenReturn(java.util.Optional.of(menuItem3));
        Mockito.when(menuItemRepository.findByName("food4")).thenReturn(java.util.Optional.of(menuItem4));
    }

/*    @Test
    public void testAddingAdmin() {

        serviceMediatorImpl.createAdmin(userRegisterDTO);

        Mockito.when(userRepository.findUserByEmail("abbbaa@gmail.com")).thenReturn(
                newAdmin);

    }*/

    @Test
    public void addMenuItemTest() throws Exception {
        serviceMediatorImpl.addMenuItemToCart(newCustomer, menuItem3.getId());
    }

    @Test
    public void addMenuItemAlreadyInCart() throws Exception {
        serviceMediatorImpl.addMenuItemToCart(newCustomer, menuItem1.getId());
        assertEquals(2, newCustomer.getCartItems().size());
    }

    @Test
    public void addMenuItemFromAnotherRestaurantTest() throws Exception {
        assertThrows(Exception.class, () -> {
            serviceMediatorImpl.addMenuItemToCart(newCustomer, menuItem4.getId());
        });

        assertEquals(2, newCustomer.getCartItems().size());
    }



}
