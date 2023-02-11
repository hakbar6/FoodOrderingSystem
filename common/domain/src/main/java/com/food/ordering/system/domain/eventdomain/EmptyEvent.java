package com.food.ordering.system.domain.eventdomain;

// digunakan untuk ending operation, karena ending operation tidak perlu return event

public final class EmptyEvent implements DomainEvent<Void>{
    public static final EmptyEvent INSTANCE = new EmptyEvent();

}
