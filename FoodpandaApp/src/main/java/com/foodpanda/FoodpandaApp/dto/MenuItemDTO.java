package com.foodpanda.FoodpandaApp.dto;

import com.foodpanda.FoodpandaApp.model.Category;
import com.foodpanda.FoodpandaApp.model.Menu;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuItemDTO {
    private String name;
    private String description;
    private float price;
    private Category category;
}
