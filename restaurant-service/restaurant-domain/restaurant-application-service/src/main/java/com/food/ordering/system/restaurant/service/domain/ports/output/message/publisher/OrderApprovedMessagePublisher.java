package com.food.ordering.system.restaurant.service.domain.ports.output.message.publisher;

import com.food.ordering.system.domain.eventdomain.publisher.DomainEventPublisher;
import com.food.ordering.system.restaurant.service.domain.event.OrderApproveEvent;

public interface OrderApprovedMessagePublisher extends DomainEventPublisher<OrderApproveEvent> {
}
