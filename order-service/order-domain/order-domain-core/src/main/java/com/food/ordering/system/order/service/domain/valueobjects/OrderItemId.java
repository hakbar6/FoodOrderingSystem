package com.food.ordering.system.order.service.domain.valueobjects;


import com.food.ordering.system.domain.valueobject.BaseId;


public class OrderItemId extends BaseId<Long> {
    public OrderItemId(Long value) {
        super(value);
    }
}
