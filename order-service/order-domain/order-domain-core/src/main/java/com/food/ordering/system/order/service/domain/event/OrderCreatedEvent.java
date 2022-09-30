package com.food.ordering.system.order.service.domain.event;

import com.food.ordering.system.domain.eventdomain.DomainEvent;
import com.food.ordering.system.order.service.domain.entity.orderProcessingAggregate.Order;

import java.time.ZonedDateTime;

public class OrderCreatedEvent implements DomainEvent<Order> {
    private final Order order;
    private final ZonedDateTime createdAt;

    public OrderCreatedEvent(Order order, ZonedDateTime createdAt) {
        this.order = order;
        this.createdAt = createdAt;
    }

    public Order getOrder() {
        return order;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }
}
