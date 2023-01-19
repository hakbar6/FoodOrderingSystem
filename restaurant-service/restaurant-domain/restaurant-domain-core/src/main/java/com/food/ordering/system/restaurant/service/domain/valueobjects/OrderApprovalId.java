package com.food.ordering.system.restaurant.service.domain.valueobjects;

import com.food.ordering.system.domain.valueobject.BaseId;

import java.util.UUID;

public class OrderApprovalId extends BaseId<UUID> {
    public OrderApprovalId(UUID value){super(value);}
}
