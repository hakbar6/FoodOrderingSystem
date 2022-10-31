package com.food.ordering.system.domain.valueobject;

import java.util.UUID;

public class OrderID extends BaseId<UUID> {
    public OrderID(UUID value) {
        super(value);
    }
}
