package com.foodpanda.FoodpandaApp.service.mapper;

import com.foodpanda.FoodpandaApp.dto.MenuItemDTO;
import com.foodpanda.FoodpandaApp.model.MenuItem;

public class MenuItemMapper {
    public static MenuItem convertFromDTO(MenuItemDTO menuItemDTO) {
        return MenuItem.builder()
                .name(menuItemDTO.getName())
                .description(menuItemDTO.getDescription())
                .price(menuItemDTO.getPrice())
                .category(menuItemDTO.getCategory())
                .build();

    }

    public static MenuItemDTO convertFromEntity(MenuItem menuItem) {
        return MenuItemDTO.builder()
                .name(menuItem.getName())
                .description(menuItem.getDescription())
                .price(menuItem.getPrice())
                .category(menuItem.getCategory())
                .build();
    }
}
