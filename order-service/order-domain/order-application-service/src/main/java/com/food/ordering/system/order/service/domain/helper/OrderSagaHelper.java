package com.food.ordering.system.order.service.domain.helper;

import com.food.ordering.system.domain.valueobject.OrderID;
import com.food.ordering.system.order.service.domain.entity.orderProcessingAggregate.Order;
import com.food.ordering.system.order.service.domain.execption.OrderNotFoundException;
import com.food.ordering.system.order.service.domain.ports.output.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class OrderSagaHelper {
    private final OrderRepository orderRepository;

    @Autowired
    public OrderSagaHelper(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order findOrder(String orderId) {
        Optional<Order> result = orderRepository.findByOrderId(new OrderID(UUID.fromString(orderId)));
        if (result.isEmpty()) {
            log.error("Order with ID : {} could not be found!", orderId);
            throw new OrderNotFoundException("Order with id " + orderId + " could not be found!");
        }

        return result.get();
    }

    public void saveOrder(Order order) {
        orderRepository.saveOrder(order);
    }
}
