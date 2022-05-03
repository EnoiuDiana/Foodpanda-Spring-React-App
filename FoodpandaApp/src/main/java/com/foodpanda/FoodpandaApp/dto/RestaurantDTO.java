package com.foodpanda.FoodpandaApp.dto;

import com.foodpanda.FoodpandaApp.model.Address;
import com.foodpanda.FoodpandaApp.model.DeliveryZone;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.CollationElementIterator;
import java.util.Collection;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantDTO {
    private String name;
    private Address address;
    private Collection<DeliveryZone> deliveryZones;
}
