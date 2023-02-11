package com.food.ordering.system.order.service.domain.mapper;

import com.food.ordering.system.domain.valueobject.CustomerID;
import com.food.ordering.system.domain.valueobject.Money;
import com.food.ordering.system.domain.valueobject.ProductID;
import com.food.ordering.system.domain.valueobject.RestaurantID;
import com.food.ordering.system.order.service.domain.dto.request.CreateOrderRequest;
import com.food.ordering.system.order.service.domain.dto.request.CreateOrderResponse;
import com.food.ordering.system.order.service.domain.dto.request.OrderAddress;
import com.food.ordering.system.order.service.domain.dto.request.OrderItemRequest;
import com.food.ordering.system.order.service.domain.dto.track.TrackOrderResponse;
import com.food.ordering.system.order.service.domain.entity.orderProcessingAggregate.Order;
import com.food.ordering.system.order.service.domain.entity.orderProcessingAggregate.OrderItem;
import com.food.ordering.system.order.service.domain.entity.orderProcessingAggregate.Product;
import com.food.ordering.system.order.service.domain.entity.restaurantAggregate.Restaurant;
import com.food.ordering.system.order.service.domain.valueobjects.StreetAddress;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class OrderDataMapper {
    public Restaurant createOrderRequestToRestaurant(CreateOrderRequest request) {
        Map<ProductID, Product> products = new HashMap<>();
        for (OrderItemRequest item: request.getItems()) {
            ProductID productID = new ProductID(item.getProductId());
            Product product = new Product(productID, null, null);
            products.put(productID,product);
        }

        return Restaurant.builder()
                .id(new RestaurantID(request.getRestaurantId()))
                .products(products)
                .build();
    }

    public Order createOrderRequestToOrderDomainEntity(CreateOrderRequest request) {
        return Order.builder()
                .customerId(new CustomerID(request.getCustomerId()))
                .restaurantID(new RestaurantID(request.getRestaurantId()))
                .deliveryAddress(addressRequestToAddressDomainEntity(request.getAddress()))
                .price(new Money(request.getPrice()))
                .items(orderItemRequestToOrderItemDomainEntity(request.getItems()))
                .build();
    }

    public CreateOrderResponse orderDomainEntityToCreateOrderResponse(Order order, String message) {
        return CreateOrderResponse.builder()
                .trackingId(order.getTrackingId().getValue())
                .orderStatus(order.getOrderStatus())
                .message(message)
                .build();
    }

    public TrackOrderResponse orderDomainEntityToTrackOrderResponse(Order order) {
        return TrackOrderResponse.builder()
                .orderTrackingId(order.getTrackingId().getValue())
                .orderStatus(order.getOrderStatus())
                .failureMessages(order.getFailureMessage())
                .build();
    }

    private List<OrderItem> orderItemRequestToOrderItemDomainEntity(List<OrderItemRequest> items) {
        return items.stream().map(
                item -> OrderItem.builder()
                        .product(new Product(new ProductID(item.getProductId()),null,null))
                        .price(new Money(item.getPrice()))
                        .quantity(item.getQuantity())
                        .subTotal(new Money(item.getSubTotal()))
                        .build()
        ).collect(Collectors.toList());
    }

    private StreetAddress addressRequestToAddressDomainEntity(OrderAddress address) {
        return new StreetAddress(
                UUID.randomUUID(),
                address.getStreet(),
                address.getPostalCode(),
                address.getCity()
        );
    }
}
