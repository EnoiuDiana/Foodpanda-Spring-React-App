package com.foodpanda.FoodpandaApp.service;

import com.foodpanda.FoodpandaApp.dto.RestaurantDTO;
import com.foodpanda.FoodpandaApp.model.Address;
import com.foodpanda.FoodpandaApp.model.Admin;
import com.foodpanda.FoodpandaApp.model.DeliveryZone;
import com.foodpanda.FoodpandaApp.model.Restaurant;
import com.foodpanda.FoodpandaApp.repository.RestaurantRepository;
import com.foodpanda.FoodpandaApp.repository.UserRepository;
import com.foodpanda.FoodpandaApp.service.mapper.RestaurantMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * service for restaurant operations
 */
@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    @Autowired
    protected RestaurantService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    /**
     * create a new restaurant
     * @param restaurantDTO restaurant details
     * @param admin the admin that has the restaurant
     * @return a new restaurant
     * @throws Exception admin already has a restaurant
     */
    protected Restaurant createRestaurant(RestaurantDTO restaurantDTO, Admin admin) throws Exception {
        if(admin.getRestaurant() == null) {
            //todo validate dto
            Restaurant restaurant = RestaurantMapper.convertFromDTO(restaurantDTO);
            return restaurantRepository.save(restaurant);
        } else {
            throw new Exception("Admin already has restaurant");
        }
    }

    /**
     * find a restaurant by id
     * @param restaurantId restaurant id
     * @return restaurant
     */
    protected Restaurant findRestaurantById(Long restaurantId) {
        return restaurantRepository.getById(restaurantId);
    }

    /**
     * find all restaurants
     * @return list of restaurants
     */
    protected List<Restaurant> findAllRestaurants(){
        return restaurantRepository.findAll();
    }


}
