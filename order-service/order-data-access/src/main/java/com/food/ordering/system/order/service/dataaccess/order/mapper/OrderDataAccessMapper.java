package com.food.ordering.system.order.service.dataaccess.order.mapper;

import com.food.ordering.system.domain.valueobject.*;
import com.food.ordering.system.order.service.dataaccess.order.entity.OrderAddressEntity;
import com.food.ordering.system.order.service.dataaccess.order.entity.OrderEntity;
import com.food.ordering.system.order.service.dataaccess.order.entity.OrderItemEntity;
import com.food.ordering.system.order.service.domain.entity.orderProcessingAggregate.Order;
import com.food.ordering.system.order.service.domain.entity.orderProcessingAggregate.OrderItem;
import com.food.ordering.system.order.service.domain.entity.orderProcessingAggregate.Product;
import com.food.ordering.system.order.service.domain.valueobjects.OrderItemId;
import com.food.ordering.system.order.service.domain.valueobjects.StreetAddress;
import com.food.ordering.system.order.service.domain.valueobjects.TrackingId;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderDataAccessMapper {
    public OrderEntity orderDomainToOrderEntity(Order order) {
        OrderEntity orderEntity = OrderEntity.builder()
                .id(order.getId().getValue())
                .orderStatus(order.getOrderStatus())
                .price(order.getPrice().getAmount())
                .items(itemToItemEntity(order.getItems()))
                .customerId(order.getCustomerId().getValue())
                .failureMessages(order.getFailureMessage().isEmpty() ?
                        "":String.join(",",order.getFailureMessage()))
                .restaurantId(order.getRestaurantID().getValue())
                .trackingId(order.getTrackingId().getValue())
                .address(addressToAddressEntity(order.getDeliveryAddress()))
                .build();

        orderEntity.getAddress().setOrder(orderEntity);
        orderEntity.getItems().forEach(
                orderItemEntity -> orderItemEntity.setOrder(orderEntity)
        );

        return orderEntity;
    }

    public Order orderEntityToOrderDomain(OrderEntity orderEntity) {
        return Order.builder()
                .id(new OrderID(orderEntity.getId()))
                .customerId(new CustomerID(orderEntity.getCustomerId()))
                .restaurantID(new RestaurantID(orderEntity.getRestaurantId()))
                .deliveryAddress(addressEntityToDeliveryAddress(orderEntity.getAddress()))
                .price(new Money(orderEntity.getPrice()))
                .items(orderItemEntityToOrderDomain(orderEntity.getItems()))
                .trackingId(new TrackingId(orderEntity.getTrackingId()))
                .orderStatus(orderEntity.getOrderStatus())
                .failureMessage(orderEntity.getFailureMessages().isEmpty() ? new ArrayList<>() :
                        new ArrayList<>(Arrays.asList(orderEntity.getFailureMessages()
                                .split(","))))
                .build();
    }

    private StreetAddress addressEntityToDeliveryAddress(OrderAddressEntity address) {
        return new StreetAddress(
                address.getId(),
                address.getStreet(),
                address.getPostalCode(),
                address.getCity()
        );
    }

    private List<OrderItem> orderItemEntityToOrderDomain(List<OrderItemEntity> items) {
        return items.stream().map(
                orderItemEntity -> OrderItem.builder()
                        .id(new OrderItemId(orderItemEntity.getId()))
                        .quantity(orderItemEntity.getQuantity())
                        .price(new Money(orderItemEntity.getPrice()))
                        .product(new Product(new ProductID(orderItemEntity.getProductId())))
                        .subTotal(new Money(orderItemEntity.getSubTotal()))
                        .build()
        ).collect(Collectors.toList());
    }

    private OrderAddressEntity addressToAddressEntity(StreetAddress deliveryAddress) {
        return OrderAddressEntity.builder()
                .street(deliveryAddress.getStreet())
                .city(deliveryAddress.getCity())
                .postalCode(deliveryAddress.getPostalCode())
                .id(deliveryAddress.getId())
                .build();
    }

    private List<OrderItemEntity> itemToItemEntity(List<OrderItem> items) {
        return items.stream().map(
          orderItem -> OrderItemEntity.builder()
                  .id(orderItem.getId().getValue())
                  .quantity(orderItem.getQuantity())
                  .productId(orderItem.getProduct().getId().getValue())
                  .price(orderItem.getPrice().getAmount())
                  .subTotal(orderItem.getSubTotal().getAmount())
                  .build()
        ).collect(Collectors.toList());
    }
}
