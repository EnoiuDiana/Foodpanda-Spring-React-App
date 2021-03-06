package com.foodpanda.FoodpandaApp.service;

import com.foodpanda.FoodpandaApp.dto.MenuItemDTO;
import com.foodpanda.FoodpandaApp.model.Admin;
import com.foodpanda.FoodpandaApp.model.Menu;
import com.foodpanda.FoodpandaApp.model.MenuItem;
import com.foodpanda.FoodpandaApp.model.Restaurant;
import com.foodpanda.FoodpandaApp.repository.MenuRepository;
import com.foodpanda.FoodpandaApp.service.mapper.MenuItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;

@Service
public class MenuService {
    private final MenuRepository menuRepository;

    @Autowired
    protected MenuService(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    protected void createMenu(Restaurant restaurant) throws Exception {
        Menu menuFound = menuRepository.findByRestaurantId(restaurant.getId());
        if(menuFound == null) {
            Menu menu = new Menu(restaurant);
            menu.setMenuItems(new HashSet<>());
            restaurant.setMenu(menu);
            menuRepository.save(menu);
        } else {
            throw new Exception("Restaurant already has a menu");
        }
    }

    protected void updateMenu(Menu menu) {
        menuRepository.save(menu);
    }

    protected Menu findMenuById(Long menuId){
        return menuRepository.findById(menuId).get();
    }
}
