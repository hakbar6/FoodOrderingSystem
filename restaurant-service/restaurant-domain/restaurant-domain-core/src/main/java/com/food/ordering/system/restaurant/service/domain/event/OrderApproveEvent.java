package com.food.ordering.system.restaurant.service.domain.event;

import com.food.ordering.system.domain.valueobject.RestaurantID;
import com.food.ordering.system.restaurant.service.domain.entity.OrderApproval;

import java.time.ZonedDateTime;
import java.util.List;

public class OrderApproveEvent extends OrderApprovalEvent{
    public OrderApproveEvent(OrderApproval orderApproval, RestaurantID restaurantID, List<String> failureMessages, ZonedDateTime createdAt) {
        super(orderApproval, restaurantID, failureMessages, createdAt);
    }
}
