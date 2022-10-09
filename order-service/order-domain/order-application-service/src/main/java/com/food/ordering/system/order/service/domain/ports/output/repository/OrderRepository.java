package com.food.ordering.system.order.service.domain.ports.output.repository;

import com.food.ordering.system.order.service.domain.entity.orderProcessingAggregate.Order;
import com.food.ordering.system.order.service.domain.valueobjects.TrackingId;

import java.util.Optional;

public interface OrderRepository {
    Order saveOrder(Order order);
    Optional<Order> findOrderByTrackingId(TrackingId trackingId);
}
