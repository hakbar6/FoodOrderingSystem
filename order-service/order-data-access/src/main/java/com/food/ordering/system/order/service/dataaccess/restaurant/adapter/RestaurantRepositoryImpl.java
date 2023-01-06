package com.food.ordering.system.order.service.dataaccess.restaurant.adapter;

import com.food.ordering.system.order.service.dataaccess.restaurant.mapper.RestaurantDataAccessMapper;
import com.food.ordering.system.order.service.dataaccess.restaurant.repository.RestaurantJpaRepository;
import com.food.ordering.system.order.service.domain.entity.restaurantAggregate.Restaurant;
import com.food.ordering.system.order.service.domain.ports.output.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RestaurantRepositoryImpl implements RestaurantRepository {

    private final RestaurantJpaRepository restaurantJpaRepository;
    private final RestaurantDataAccessMapper mapper;

    @Autowired
    public RestaurantRepositoryImpl(RestaurantJpaRepository restaurantJpaRepository,
                                    RestaurantDataAccessMapper mapper) {
        this.restaurantJpaRepository = restaurantJpaRepository;
        this.mapper = mapper;
    }


    /***
     Kenapa query repository nya seperti itu?

     Perlu kita ketahui bahwa relasi restaurant dan entity adalah one to many.
     Pada kasus ini, DB kita menerapkan teknik pemetaan dengan membuat sebuah entity
     baru yang memiliki foreign key ke restaurant dan juga product.

     Jika penasaran, liat saja langsung ke db nya, dengan nama table restaurant_product pada schema
     restaurant
    ***/
    @Override
    public Optional<Restaurant> findRestaurantInformation(Restaurant restaurant) {
        return restaurantJpaRepository.findById_RestaurantIdAndId_ProductIdIn(
                restaurant.getId().getValue(),
                mapper.restaurantToRestaurantProductsId(restaurant))
                .map(mapper::listRestaurantEntityToRestaurant);
    }
}
