package com.food.ordering.system.restaurant.service.domain.entity;

import com.food.ordering.system.domain.entity.BaseEntity;
import com.food.ordering.system.domain.valueobject.OrderApprovalStatus;
import com.food.ordering.system.domain.valueobject.OrderID;
import com.food.ordering.system.domain.valueobject.RestaurantID;
import com.food.ordering.system.restaurant.service.domain.valueobjects.OrderApprovalId;

/*
Merupakan result entity setelah melakukan business logic check pada restaurant service
*/

public class OrderApproval extends BaseEntity<OrderApprovalId> {
    private final RestaurantID restaurantID;
    private final OrderID orderID;
    private final OrderApprovalStatus approvalStatus;

    private OrderApproval(Builder builder) {
        setId(builder.orderApprovalId);
        restaurantID = builder.restaurantID;
        orderID = builder.orderID;
        approvalStatus = builder.approvalStatus;
    }

    public static Builder builder() {
        return new Builder();
    }

    public RestaurantID getRestaurantID() {
        return restaurantID;
    }

    public OrderID getOrderID() {
        return orderID;
    }

    public OrderApprovalStatus getApprovalStatus() {
        return approvalStatus;
    }


    public static final class Builder {
        private OrderApprovalId orderApprovalId;
        private RestaurantID restaurantID;
        private OrderID orderID;
        private OrderApprovalStatus approvalStatus;

        private Builder() {
        }

        public Builder orderApprovalId(OrderApprovalId val) {
            orderApprovalId = val;
            return this;
        }

        public Builder restaurantID(RestaurantID val) {
            restaurantID = val;
            return this;
        }

        public Builder orderID(OrderID val) {
            orderID = val;
            return this;
        }

        public Builder approvalStatus(OrderApprovalStatus val) {
            approvalStatus = val;
            return this;
        }

        public OrderApproval build() {
            return new OrderApproval(this);
        }
    }
}
