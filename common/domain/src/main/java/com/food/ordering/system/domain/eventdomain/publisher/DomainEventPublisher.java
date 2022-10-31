package com.food.ordering.system.domain.eventdomain.publisher;

import com.food.ordering.system.domain.eventdomain.DomainEvent;

public interface DomainEventPublisher<T extends DomainEvent> {

    void publish(T domainEvent);
}
