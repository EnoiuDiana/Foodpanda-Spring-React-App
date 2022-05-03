package com.foodpanda.FoodpandaApp.service;

import com.foodpanda.FoodpandaApp.dto.CategoryDTO;
import com.foodpanda.FoodpandaApp.dto.MenuItemDTO;
import com.foodpanda.FoodpandaApp.model.Admin;
import com.foodpanda.FoodpandaApp.model.Menu;
import com.foodpanda.FoodpandaApp.model.MenuItem;
import com.foodpanda.FoodpandaApp.model.Restaurant;
import com.foodpanda.FoodpandaApp.repository.MenuItemRepository;
import com.foodpanda.FoodpandaApp.service.mapper.MenuItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class MenuItemService {
    private final MenuItemRepository menuItemRepository;

    @Autowired
    protected MenuItemService(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    protected void createMenuItem(MenuItemDTO menuItemDTO, Admin admin) throws Exception {
        //todo validate dto
        Restaurant restaurant = admin.getRestaurant();
        Menu menu = restaurant.getMenu();
        if(menu != null) {
            MenuItem menuItem = MenuItemMapper.convertFromDTO(menuItemDTO);
            menuItem.setMenu(menu);
            Set<MenuItem> menuItemList= menu.getMenuItems();
            menuItemList.add(menuItem);
            menu.setMenuItems(menuItemList);
            menuItemRepository.save(menuItem);

        } else {
            throw new Exception("Menu does not exists");
        }

    }

    protected List<MenuItem> findMenuItemsByMenu(Menu menu) {
        return menuItemRepository.findAllByMenuId(menu.getId());
    }

    protected MenuItem findMenuItemById(Long menuItemId){
        return menuItemRepository.findById(menuItemId).get();
    }
}
