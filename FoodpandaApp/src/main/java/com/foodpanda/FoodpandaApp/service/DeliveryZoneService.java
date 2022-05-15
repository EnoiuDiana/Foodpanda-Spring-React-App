package com.foodpanda.FoodpandaApp.service;

import com.foodpanda.FoodpandaApp.model.DeliveryZone;
import com.foodpanda.FoodpandaApp.repository.DeliveryZoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * service for delivery zones operations
 */
@Service
public class DeliveryZoneService {

    final DeliveryZoneRepository deliveryZoneRepository;

    @Autowired
    public DeliveryZoneService(DeliveryZoneRepository deliveryZoneRepository) {
        this.deliveryZoneRepository = deliveryZoneRepository;
    }

    /**
     * retrieve all delivery zones
     * @return
     */
    public List<DeliveryZone> findAll() {
        return deliveryZoneRepository.findAll();
    }
}
