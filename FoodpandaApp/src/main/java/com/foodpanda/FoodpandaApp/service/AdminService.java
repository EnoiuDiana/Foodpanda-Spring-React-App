package com.foodpanda.FoodpandaApp.service;

import com.foodpanda.FoodpandaApp.dto.UserRegisterDTO;
import com.foodpanda.FoodpandaApp.model.Admin;
import com.foodpanda.FoodpandaApp.model.Customer;
import com.foodpanda.FoodpandaApp.model.Restaurant;
import com.foodpanda.FoodpandaApp.model.User;
import com.foodpanda.FoodpandaApp.repository.AdminRepository;
import com.foodpanda.FoodpandaApp.repository.UserRepository;
import com.foodpanda.FoodpandaApp.service.Validators.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * the service for admin operations
 */
@Service
public class AdminService {
    private final AdminRepository adminRepository;
    private final UserRepository userRepository;

    @Autowired
    public AdminService(AdminRepository adminRepository, UserRepository userRepository) {
        this.adminRepository = adminRepository;
        this.userRepository = userRepository;
    }

    /**
     * set a restaurant to an admin
     * @param admin the admin
     * @param restaurant the restaurant
     */
    public void setRestaurant(Admin admin, Restaurant restaurant) {
        admin.setRestaurant(restaurant);
        adminRepository.save(admin);
    }

    /**
     * create a new admin account
     * @param userRegisterDTO admin account details
     */
    public void createAdmin(UserRegisterDTO userRegisterDTO) {
        UserValidator userValidator = new UserValidator(userRepository);
        try{
            userValidator.validateUser(userRegisterDTO.getEmail(), userRegisterDTO.getPassword());
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            User userAdmin = new Admin(userRegisterDTO.getEmail(),
                    encoder.encode(userRegisterDTO.getPassword()),
                    /*                    userRegisterDTO.getPassword(),*/
                    userRegisterDTO.getFirstName(),
                    userRegisterDTO.getLastName());
            adminRepository.save(userAdmin);

        } catch (Exception exception) {
            System.out.println("User not created");
        }
    }
}
