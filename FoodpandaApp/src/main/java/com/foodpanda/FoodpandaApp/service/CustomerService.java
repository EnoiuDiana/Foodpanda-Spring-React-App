package com.foodpanda.FoodpandaApp.service;

import com.foodpanda.FoodpandaApp.dto.UserRegisterDTO;
import com.foodpanda.FoodpandaApp.model.Customer;
import com.foodpanda.FoodpandaApp.model.User;
import com.foodpanda.FoodpandaApp.repository.CustomerRepository;
import com.foodpanda.FoodpandaApp.repository.UserRepository;
import com.foodpanda.FoodpandaApp.service.Validators.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * service for customer operations
 */
@Service
public class CustomerService {

    final CustomerRepository customerRepository;
    final UserRepository userRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, UserRepository userRepository) {
        this.customerRepository = customerRepository;
        this.userRepository = userRepository;
    }

    /**
     * create a new customer account
     * @param userRegisterDTO account details
     */
    public void createCustomer(UserRegisterDTO userRegisterDTO) {
        UserValidator userValidator = new UserValidator(userRepository);
        try{
            userValidator.validateUser(userRegisterDTO.getEmail(), userRegisterDTO.getPassword());
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            User userCustomer = new Customer(userRegisterDTO.getEmail(),
                    encoder.encode(userRegisterDTO.getPassword()),
/*                    userRegisterDTO.getPassword(),*/
                    userRegisterDTO.getFirstName(),
                    userRegisterDTO.getLastName());
            customerRepository.save(userCustomer);

        } catch (Exception exception) {
            System.out.println("User not created");
        }
    }

}
