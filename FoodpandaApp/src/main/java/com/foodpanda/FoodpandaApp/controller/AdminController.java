package com.foodpanda.FoodpandaApp.controller;

import com.foodpanda.FoodpandaApp.dto.MenuItemDTO;
import com.foodpanda.FoodpandaApp.dto.RestaurantDTO;
import com.foodpanda.FoodpandaApp.dto.UserRegisterDTO;
import com.foodpanda.FoodpandaApp.model.*;
import com.foodpanda.FoodpandaApp.repository.UserRepository;
import com.foodpanda.FoodpandaApp.security.services.UserDetailsImpl;
import com.foodpanda.FoodpandaApp.service.IServiceFacade;
import com.foodpanda.FoodpandaApp.service.MenuPdfGenerator.GeneratePDFMenu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final UserRepository userRepository;
    private final IServiceFacade serviceFacade;

    private static final Logger logger = LoggerFactory.
            getLogger(AdminController.class);


    @Autowired
    public AdminController(UserRepository userRepository, IServiceFacade serviceFacade) {
        this.userRepository = userRepository;
        this.serviceFacade = serviceFacade;
    }

    @PostMapping("/createAdmin")
    public ResponseEntity<String> createUser(@RequestBody UserRegisterDTO userRegisterDTO) {
        serviceFacade.createAdmin(userRegisterDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/addRestaurant")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> addRestaurantToAdmin(@RequestBody RestaurantDTO restaurantDTO) {
        logger.info(String.format("Adding restaurant %s to admin", restaurantDTO.getName()));
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User loggedInUser = userDetails.getUser();
        //User loggedInUser = UserController.loggedInUser;
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
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> addMenuItem(@RequestBody MenuItemDTO menuItemDTO) {
        logger.info(String.format("Adding menu item %s to menu for admin", menuItemDTO.getName()));
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User loggedInUser = userDetails.getUser();
        //User loggedInUser = UserController.loggedInUser;
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
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> createMenu() {
        logger.info("Creating a new menu for admin");
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User loggedInUser = userDetails.getUser();
        //User loggedInUser = UserController.loggedInUser;
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
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> createMenu(@PathVariable Long menuId, @PathVariable Long menuItemId) {
        logger.info("Creating a new menu for admin");
        serviceFacade.addMenuItemsToMenu(menuId, menuItemId);
        return ResponseEntity.status(HttpStatus.OK).body("Added menu items to menu");
    }

    @GetMapping("/viewMenuForAdmin")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Set<MenuItem>> viewMenuForAdmin() {
        logger.info("View menu for admin");
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User loggedInUser = userDetails.getUser();
        //User loggedInUser = UserController.loggedInUser;
        return ResponseEntity.status(HttpStatus.OK).body(serviceFacade.getMenuItemsForAdmin((Admin) loggedInUser));
    }

    @GetMapping("/getDeliveryZones")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<DeliveryZone>> getDeliveryZones() {
        return ResponseEntity.status(HttpStatus.OK).body(serviceFacade.getDeliveryZones());
    }

    @GetMapping("/getMenuId")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Long> getMenuId() {
        logger.info("Get the menu id for admin");
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User loggedInUser = userDetails.getUser();
        //User loggedInUser = UserController.loggedInUser;
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
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<Order>> getOrders() {
        logger.info("Get all orders for admin");
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User loggedInUser = userDetails.getUser();
        //User loggedInUser = UserController.loggedInUser;
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
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> acceptOrder(@PathVariable Long orderId) {
        logger.info("accept an order for admin");
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User loggedInUser = userDetails.getUser();
        //User loggedInUser = UserController.loggedInUser;
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
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> declineOrder(@PathVariable Long orderId) {
        logger.info("decline an order for admin");
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User loggedInUser = userDetails.getUser();
        //User loggedInUser = UserController.loggedInUser;
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
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> updateOrder(@PathVariable Long orderId) {
        logger.info("Update an order for admin");
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User loggedInUser = userDetails.getUser();
        //User loggedInUser = UserController.loggedInUser;
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

    @GetMapping(value = "/exportPdf", produces =
            MediaType.APPLICATION_PDF_VALUE)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<InputStreamResource> exportMenuAsPdf() {
        logger.info("Exporting the menu as pdf");
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User loggedInUser = userDetails.getUser();
        //User loggedInUser = UserController.loggedInUser;
        if (loggedInUser instanceof Admin) {
            try {
                ByteArrayInputStream bis = serviceFacade.exportMenuAsPDF((Admin) loggedInUser);
                HttpHeaders headers = new HttpHeaders();
                headers.add("Content-Disposition", "inline; filename=menu.pdf");
                return ResponseEntity.ok().headers(headers).contentType
                        (MediaType.APPLICATION_PDF)
                        .body(new InputStreamResource(bis));
            } catch (Exception exception) {
                exception.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

}
