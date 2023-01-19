package com.food.ordering.system.restaurant.service.messaging.mapper;

import com.food.ordering.system.domain.valueobject.RestaurantOrderStatus;
import com.food.ordering.system.kafka.order.avro.model.OrderApprovalStatus;
import com.food.ordering.system.kafka.order.avro.model.RestaurantApprovalRequestAvroModel;
import com.food.ordering.system.kafka.order.avro.model.RestaurantApprovalResponseAvroModel;
import com.food.ordering.system.restaurant.service.domain.dto.request.ProductRequest;
import com.food.ordering.system.restaurant.service.domain.dto.request.RestaurantApprovalRequest;
import com.food.ordering.system.restaurant.service.domain.event.OrderApproveEvent;
import com.food.ordering.system.restaurant.service.domain.event.OrderRejectedEvent;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class RestaurantMessageDataMapper {
    public RestaurantApprovalResponseAvroModel
    orderApproveEventToRestaurantApprovalResponseAvroModel(OrderApproveEvent orderApproveEvent) {
        return RestaurantApprovalResponseAvroModel.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setSagaId("")
                .setOrderId(orderApproveEvent.getOrderApproval().getOrderID().getValue().toString())
                .setRestaurantId(orderApproveEvent.getRestaurantID().getValue().toString())
                .setFailureMessages(orderApproveEvent.getFailureMessages())
                .setOrderApprovalStatus(OrderApprovalStatus.valueOf(orderApproveEvent.getOrderApproval()
                        .getApprovalStatus().name()))
                .setCreatedAt(orderApproveEvent.getCreatedAt().toInstant())
                .build();
    }

    public RestaurantApprovalResponseAvroModel
    orderRejectedEventToRestaurantApprovalResponseAvroModel(OrderRejectedEvent orderRejectedEvent) {
        return RestaurantApprovalResponseAvroModel.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setSagaId("")
                .setOrderId(orderRejectedEvent.getOrderApproval().getOrderID().getValue().toString())
                .setRestaurantId(orderRejectedEvent.getRestaurantID().getValue().toString())
                .setFailureMessages(orderRejectedEvent.getFailureMessages())
                .setOrderApprovalStatus(OrderApprovalStatus.valueOf(orderRejectedEvent.getOrderApproval()
                        .getApprovalStatus().name()))
                .setCreatedAt(orderRejectedEvent.getCreatedAt().toInstant())
                .build();
    }

    public RestaurantApprovalRequest
    restaurantApprovalRequestAvroModelToRestaurantApprovalRequest(RestaurantApprovalRequestAvroModel requestAvroModel) {
        return RestaurantApprovalRequest.builder()
                .restaurantId(requestAvroModel.getRestaurantId())
                .createdAt(requestAvroModel.getCreatedAt())
                .id(requestAvroModel.getId())
                .orderId(requestAvroModel.getOrderId())
                .price(requestAvroModel.getPrice())
                .products(requestAvroModel.getProducts().stream().map(product -> ProductRequest.builder()
                        .productId(product.getId())
                        .quantity(product.getQuantity())
                        .build()).collect(Collectors.toList()))
                .sagaId(requestAvroModel.getSagaId())
                .restaurantOrderStatus(RestaurantOrderStatus.valueOf(requestAvroModel.getRestaurantOrderStatus().name()))
                .build();
    }
}
