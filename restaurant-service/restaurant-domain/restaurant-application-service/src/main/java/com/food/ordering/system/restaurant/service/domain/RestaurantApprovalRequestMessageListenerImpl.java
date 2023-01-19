package com.food.ordering.system.restaurant.service.domain;

import com.food.ordering.system.restaurant.service.domain.dto.request.RestaurantApprovalRequest;
import com.food.ordering.system.restaurant.service.domain.event.OrderApprovalEvent;
import com.food.ordering.system.restaurant.service.domain.event.OrderApproveEvent;
import com.food.ordering.system.restaurant.service.domain.event.OrderRejectedEvent;
import com.food.ordering.system.restaurant.service.domain.ports.input.message.listener.RestaurantApprovalRequestMessageListener;
import com.food.ordering.system.restaurant.service.domain.ports.output.message.publisher.OrderApprovedMessagePublisher;
import com.food.ordering.system.restaurant.service.domain.ports.output.message.publisher.OrderRejectedMessagePublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RestaurantApprovalRequestMessageListenerImpl implements RestaurantApprovalRequestMessageListener {

    private final RestaurantApprovalRequestHelper restaurantApprovalRequestHelper;
    private final OrderApprovedMessagePublisher orderApprovedMessagePublisher;
    private final OrderRejectedMessagePublisher orderRejectedMessagePublisher;

    @Autowired
    public RestaurantApprovalRequestMessageListenerImpl(RestaurantApprovalRequestHelper restaurantApprovalRequestHelper,
                                                        OrderApprovedMessagePublisher orderApprovedMessagePublisher,
                                                        OrderRejectedMessagePublisher orderRejectedMessagePublisher) {
        this.restaurantApprovalRequestHelper = restaurantApprovalRequestHelper;
        this.orderApprovedMessagePublisher = orderApprovedMessagePublisher;
        this.orderRejectedMessagePublisher = orderRejectedMessagePublisher;
    }

    @Override
    public void approveOrder(RestaurantApprovalRequest restaurantApprovalRequest) {
        OrderApprovalEvent orderApprovalEvent = restaurantApprovalRequestHelper.persistOrderApproval(restaurantApprovalRequest);
        fireEvent(orderApprovalEvent);
    }

    private void fireEvent(OrderApprovalEvent orderApprovalEvent) {
        log.info("Publishing restaurant approval event with restaurant id : {} and order id : {}",
                orderApprovalEvent.getRestaurantID(),
                orderApprovalEvent.getOrderApproval().getOrderID());
        if (orderApprovalEvent instanceof OrderApproveEvent) {
            orderApprovedMessagePublisher.publish((OrderApproveEvent) orderApprovalEvent);
        } else if (orderApprovalEvent instanceof OrderRejectedEvent) {
            orderRejectedMessagePublisher.publish((OrderRejectedEvent) orderApprovalEvent);
        }
    }
}
