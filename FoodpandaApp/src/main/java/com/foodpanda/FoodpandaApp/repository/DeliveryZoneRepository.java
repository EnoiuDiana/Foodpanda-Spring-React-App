package com.foodpanda.FoodpandaApp.repository;

import com.foodpanda.FoodpandaApp.model.DeliveryZone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryZoneRepository extends JpaRepository<DeliveryZone, Long> {
}
