package com.food.ordering.system.order.service.dataaccess.restaurant.mapper;

import com.food.ordering.system.domain.valueobject.Money;
import com.food.ordering.system.domain.valueobject.ProductID;
import com.food.ordering.system.domain.valueobject.RestaurantID;
import com.food.ordering.system.order.service.dataaccess.restaurant.entity.RestaurantEntity;
import com.food.ordering.system.order.service.dataaccess.restaurant.exception.RestaurantDataAccessException;
import com.food.ordering.system.order.service.domain.entity.orderProcessingAggregate.Product;
import com.food.ordering.system.order.service.domain.entity.restaurantAggregate.Restaurant;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class RestaurantDataAccessMapper {

    public List<UUID> restaurantToRestaurantProducts(Restaurant restaurant) {
        return restaurant.getProducts().values().stream()
                .map(product -> product.getId().getValue())
                .collect(Collectors.toList());
    }

    public Restaurant listRestaurantEntityToRestaurant(List<RestaurantEntity> restaurantEntities) {
        RestaurantEntity restaurantEntity =  restaurantEntities.stream().findFirst().orElseThrow(
                () -> new RestaurantDataAccessException("Restaurant could not be found!")
        );

        Map<ProductID,Product> productMap = restaurantEntities.stream().map(
                entity -> new Product(new ProductID(entity.getId().getProductId()),
                        entity.getProductName(),
                        new Money(entity.getProductPrice())))
                .collect(Collectors.toMap(Product::getId,product -> product));

        return Restaurant.builder()
                .products(productMap)
                .id(new RestaurantID(restaurantEntity.getId().getRestaurantId()))
                .active(restaurantEntity.getRestaurantActive())
                .build();
    }
}
