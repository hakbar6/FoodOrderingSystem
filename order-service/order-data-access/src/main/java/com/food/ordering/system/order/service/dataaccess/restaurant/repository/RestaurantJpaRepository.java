package com.food.ordering.system.order.service.dataaccess.restaurant.repository;

import com.food.ordering.system.order.service.dataaccess.restaurant.entity.RestaurantEntity;
import com.food.ordering.system.order.service.dataaccess.restaurant.entity.RestaurantEntityId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RestaurantJpaRepository extends JpaRepository<RestaurantEntity, RestaurantEntityId> {
    Optional<List<RestaurantEntity>> findById_RestaurantIdAndId_ProductIdIn(UUID restaurantId, List<UUID> productId);
}


/***
Kita membuat pendekatan seperti diatas, alasannya karena hubungan one to many relationship antara
restaurant dan juga product didefinisikan dengan membuat sebuah entitas baru pada database, yang memiliki
foreign key yang merujuk kepada restaurant dan juga product
 ***/
