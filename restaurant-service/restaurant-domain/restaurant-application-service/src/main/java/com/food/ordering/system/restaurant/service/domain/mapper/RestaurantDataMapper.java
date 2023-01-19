package com.food.ordering.system.restaurant.service.domain.mapper;

import com.food.ordering.system.domain.valueobject.*;
import com.food.ordering.system.restaurant.service.domain.dto.request.ProductRequest;
import com.food.ordering.system.restaurant.service.domain.dto.request.RestaurantApprovalRequest;
import com.food.ordering.system.restaurant.service.domain.entity.OrderDetail;
import com.food.ordering.system.restaurant.service.domain.entity.Product;
import com.food.ordering.system.restaurant.service.domain.entity.Restaurant;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class RestaurantDataMapper {
    public Restaurant restaurantApprovalRequestToRestaurant(RestaurantApprovalRequest request) {
        Map<ProductID, Product> products = new HashMap<>();
        for (ProductRequest productRequest: request.getProducts()) {
            products.put(
                    new ProductID(UUID.fromString(productRequest.getProductId())),
                    Product.builder()
                            .productId(new ProductID(UUID.fromString(productRequest.getProductId())))
                            .quantity(productRequest.getQuantity())
                            .build()
            );
        }

        return Restaurant.builder()
                .restaurantID(new RestaurantID(UUID.fromString(request.getRestaurantId())))
                .orderDetail(OrderDetail.builder()
                        .orderID(new OrderID(UUID.fromString(request.getOrderId())))
                        .products(products)
                        .totalAmount(new Money(request.getPrice()))
                        .orderStatus(OrderStatus.valueOf(request.getRestaurantOrderStatus().name()))
                        .build())
                .build();
    }
}
