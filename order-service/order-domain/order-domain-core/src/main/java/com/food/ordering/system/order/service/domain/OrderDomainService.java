package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.order.service.domain.entity.orderProcessingAggregate.Order;
import com.food.ordering.system.order.service.domain.entity.restaurantAggregate.Restaurant;
import com.food.ordering.system.order.service.domain.event.OrderCancelledEvent;
import com.food.ordering.system.order.service.domain.event.OrderCreatedEvent;
import com.food.ordering.system.order.service.domain.event.OrderPaidEvent;

import java.util.List;

public interface OrderDomainService {
    OrderCreatedEvent initializeAndValidateOrder(Order order, Restaurant restaurant);

    OrderPaidEvent payOrder(Order order);

    OrderCancelledEvent cancelOrder(Order order, List<String> failureMessages);

    void approveOrder(Order order);

    void cancellingOrder(Order order, List<String> failureMessages);
}
