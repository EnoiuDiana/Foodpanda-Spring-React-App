package com.foodpanda.FoodpandaApp.controller;

import com.foodpanda.FoodpandaApp.dto.MenuItemDTO;
import com.foodpanda.FoodpandaApp.dto.RestaurantDTO;
import com.foodpanda.FoodpandaApp.model.*;
import com.foodpanda.FoodpandaApp.repository.UserRepository;
import com.foodpanda.FoodpandaApp.service.IServiceFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final UserRepository userRepository;
    private final IServiceFacade serviceFacade;

    @Autowired
    public AdminController(UserRepository userRepository, IServiceFacade serviceFacade) {
        this.userRepository = userRepository;
        this.serviceFacade = serviceFacade;
    }

    @PostMapping("/createAdmin")
    public ResponseEntity<String> createUser(@RequestBody Admin admin) {
        userRepository.save(admin);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/addRestaurant")
    public ResponseEntity<String> addRestaurantToAdmin(@RequestBody RestaurantDTO restaurantDTO) {
        User loggedInUser = UserController.loggedInUser;
        if (loggedInUser instanceof Admin) {
            try {
                serviceFacade.createNewRestaurant(restaurantDTO, (Admin) loggedInUser);
            } catch (Exception exception) {
                exception.printStackTrace();
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Could not add restaurant");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not allowed to add restaurant");
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/addMenuItem")
    public ResponseEntity<String> addMenuItem(@RequestBody MenuItemDTO menuItemDTO) {
        User loggedInUser = UserController.loggedInUser;
        if (loggedInUser instanceof Admin) {
            try {
                serviceFacade.createNewMenuItem(menuItemDTO, (Admin) loggedInUser);
            } catch (Exception exception) {
                exception.printStackTrace();
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Could not create menu item");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not allowed to add menu items");
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/createMenu")
    public ResponseEntity<String> createMenu() {
        User loggedInUser = UserController.loggedInUser;
        if (loggedInUser instanceof Admin) {
            try {
                serviceFacade.createNewMenu(((Admin) loggedInUser).getRestaurant());
            } catch (Exception exception) {
                exception.printStackTrace();
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Could not create menu");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not allowed to add menu");
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/menu/{menuId}/menuItem/{menuItemId}")
    public ResponseEntity<String> createMenu(@PathVariable Long menuId, @PathVariable Long menuItemId) {
        serviceFacade.addMenuItemsToMenu(menuId, menuItemId);
        return ResponseEntity.status(HttpStatus.OK).body("Added menu items to menu");
    }

    @GetMapping("/viewMenuForAdmin")
    public ResponseEntity<Set<MenuItem>> viewMenuForAdmin() {
        User loggedInUser = UserController.loggedInUser;
        return ResponseEntity.status(HttpStatus.OK).body(serviceFacade.getMenuItemsForAdmin((Admin) loggedInUser));
    }

    @GetMapping("/getDeliveryZones")
    public ResponseEntity<List<DeliveryZone>> getDeliveryZones() {
        return ResponseEntity.status(HttpStatus.OK).body(serviceFacade.getDeliveryZones());
    }

    @GetMapping("/getMenuId")
    public ResponseEntity<Long> getMenuId() {
        User loggedInUser = UserController.loggedInUser;
        long menuId;
        if (loggedInUser instanceof Admin) {
            try {
                menuId = serviceFacade.getMenuId((Admin) loggedInUser);
            } catch (Exception exception) {
                exception.printStackTrace();
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(menuId);
    }

    @GetMapping("/getOrdersForAdmin")
    public ResponseEntity<List<Order>> getOrders() {
        User loggedInUser = UserController.loggedInUser;
        if (loggedInUser instanceof Admin) {
            try {
                return ResponseEntity.status(HttpStatus.OK).body(serviceFacade.getAllOrders((Admin) loggedInUser));
            } catch (Exception exception) {
                exception.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @PostMapping("/acceptOrder/orderId/{orderId}")
    public ResponseEntity<String> acceptOrder(@PathVariable Long orderId) {
        User loggedInUser = UserController.loggedInUser;
        if (loggedInUser instanceof Admin) {
            try {
                serviceFacade.changeOrderStatus(orderId, Status.ACCEPTED);
                return ResponseEntity.status(HttpStatus.OK).body("Order accepted");
            } catch (Exception exception) {
                exception.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @PostMapping("/declineOrder/orderId/{orderId}")
    public ResponseEntity<String> declineOrder(@PathVariable Long orderId) {
        User loggedInUser = UserController.loggedInUser;
        if (loggedInUser instanceof Admin) {
            try {
                serviceFacade.changeOrderStatus(orderId, Status.DECLINED);
                return ResponseEntity.status(HttpStatus.OK).body("Order declined");
            } catch (Exception exception) {
                exception.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @PostMapping("/updateOrder/orderId/{orderId}")
    public ResponseEntity<String> updateOrder(@PathVariable Long orderId) {
        User loggedInUser = UserController.loggedInUser;
        if (loggedInUser instanceof Admin) {
            try {
                serviceFacade.updateOrderStatus(orderId);
                return ResponseEntity.status(HttpStatus.OK).body("Order updated");
            } catch (Exception exception) {
                exception.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

}
