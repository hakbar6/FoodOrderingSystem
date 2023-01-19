package com.food.ordering.system.restaurant.service.dataaccess.orderApproval.mapper;

import com.food.ordering.system.domain.valueobject.OrderID;
import com.food.ordering.system.domain.valueobject.RestaurantID;
import com.food.ordering.system.restaurant.service.dataaccess.orderApproval.entity.OrderApprovalEntity;
import com.food.ordering.system.restaurant.service.domain.entity.OrderApproval;
import com.food.ordering.system.restaurant.service.domain.valueobjects.OrderApprovalId;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class OrderApprovalDataAccessMapper {
    public OrderApprovalEntity orderApprovalDomainToOrderApprovalEntity(OrderApproval orderApproval){
        return OrderApprovalEntity.builder()
                .id(orderApproval.getOrderID().getValue())
                .restaurantId(orderApproval.getRestaurantID().getValue())
                .orderId(orderApproval.getOrderID().getValue())
                .status(orderApproval.getApprovalStatus())
                .build();
    }

    public OrderApproval orderApprovalEntityToOrderApprovalDomain(OrderApprovalEntity orderApprovalEntity){
        return OrderApproval.builder()
                .orderApprovalId(new OrderApprovalId(orderApprovalEntity.getId()))
                .restaurantID(new RestaurantID(orderApprovalEntity.getRestaurantId()))
                .orderID(new OrderID(orderApprovalEntity.getOrderId()))
                .approvalStatus(orderApprovalEntity.getStatus())
                .build();
    }
}
