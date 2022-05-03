package com.foodpanda.FoodpandaApp.service;

import com.foodpanda.FoodpandaApp.model.Admin;
import com.foodpanda.FoodpandaApp.model.Restaurant;
import com.foodpanda.FoodpandaApp.model.User;
import com.foodpanda.FoodpandaApp.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminService {
    private final AdminRepository adminRepository;

    @Autowired
    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    public void setRestaurant(Admin admin, Restaurant restaurant) {
        admin.setRestaurant(restaurant);
        adminRepository.save(admin);
    }
}
