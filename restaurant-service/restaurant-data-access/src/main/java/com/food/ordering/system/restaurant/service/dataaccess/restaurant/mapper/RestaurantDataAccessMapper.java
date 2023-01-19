package com.food.ordering.system.restaurant.service.dataaccess.restaurant.mapper;

import com.food.ordering.system.domain.valueobject.Money;
import com.food.ordering.system.domain.valueobject.ProductID;
import com.food.ordering.system.domain.valueobject.RestaurantID;
import com.food.ordering.system.restaurant.service.dataaccess.restaurant.entity.RestaurantEntity;
import com.food.ordering.system.restaurant.service.dataaccess.restaurant.exception.RestaurantDataAccessException;
import com.food.ordering.system.restaurant.service.domain.entity.OrderDetail;
import com.food.ordering.system.restaurant.service.domain.entity.Product;
import com.food.ordering.system.restaurant.service.domain.entity.Restaurant;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class RestaurantDataAccessMapper {

    public List<UUID> restaurantToRestaurantProductsId(Restaurant restaurant) {
        return restaurant.getOrderDetail().getProducts().values().stream()
                .map(product -> product.getId().getValue())
                .collect(Collectors.toList());
    }

    public Restaurant listRestaurantEntityToRestaurant(List<RestaurantEntity> restaurantEntities) {
        RestaurantEntity restaurantEntity =  restaurantEntities.stream().findFirst().orElseThrow(
                () -> new RestaurantDataAccessException("Restaurant could not be found!")
        );

        Map<ProductID, Product> productMap = restaurantEntities.stream().map(
                        entity -> Product.builder()
                                .productId(new ProductID(entity.getId().getProductId()))
                                .name(entity.getProductName())
                                .price(new Money(entity.getProductPrice()))
                                .available(entity.getProductAvailable())
                                .build())
                .collect(Collectors.toMap(Product::getId,product -> product));

        return Restaurant.builder()
                .restaurantID(new RestaurantID(restaurantEntity.getId().getRestaurantId()))
                .orderDetail(OrderDetail.builder()
                        .products(productMap)
                        .build())
                .active(restaurantEntity.getRestaurantActive())
                .build();
    }
}
