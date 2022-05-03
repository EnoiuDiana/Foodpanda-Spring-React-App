package com.foodpanda.FoodpandaApp.controller;

import com.foodpanda.FoodpandaApp.dto.UserLoginDTO;
import com.foodpanda.FoodpandaApp.model.Admin;
import com.foodpanda.FoodpandaApp.model.User;
import com.foodpanda.FoodpandaApp.model.UserType;
import com.foodpanda.FoodpandaApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    private final UserRepository userRepository;
    public static User loggedInUser;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/getUsers")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(userRepository.findAll());
    }

    // Not secure todo use spring security
    @PostMapping("/login")
    public ResponseEntity<UserType> login(@RequestBody UserLoginDTO userLoginDTO) {
        User user = userRepository.findUserByEmailAndPassword(userLoginDTO.getEmail(), userLoginDTO.getPassword());
        if(user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        loggedInUser = user;
        if(user instanceof Admin) {
            return ResponseEntity.status(HttpStatus.OK).body(UserType.ADMIN);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(UserType.CUSTOMER);
        }
    }

}
