package com.foodpanda.FoodpandaApp.service.mapper;

import com.foodpanda.FoodpandaApp.dto.RestaurantDTO;
import com.foodpanda.FoodpandaApp.model.Restaurant;

public class RestaurantMapper {
    public static Restaurant convertFromDTO(RestaurantDTO restaurantDTO) {
        return Restaurant.builder()
                .name(restaurantDTO.getName())
                .address(restaurantDTO.getAddress())
                .deliveryZones(restaurantDTO.getDeliveryZones())
                .build();

    }

    public static RestaurantDTO convertFromEntity (Restaurant restaurant) {
        return RestaurantDTO.builder()
                .name(restaurant.getName())
                .address(restaurant.getAddress())
                .deliveryZones(restaurant.getDeliveryZones())
                .build();
    }
}
