package com.food.ordering.system.order.service.domain.valueobjects;

import com.food.ordering.system.domain.valueobject.BaseID;

import java.util.UUID;

public class OrderItemId extends BaseID<Long> {
    public OrderItemId(Long value) {
        super(value);
    }
}
