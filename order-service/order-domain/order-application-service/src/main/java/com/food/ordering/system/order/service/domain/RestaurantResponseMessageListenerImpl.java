package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.order.service.domain.dto.response.RestaurantApprovalResponse;
import com.food.ordering.system.order.service.domain.event.OrderCancelledEvent;
import com.food.ordering.system.order.service.domain.ports.input.message.listener.restaurant.RestaurantApprovalResponseMessageListener;
import com.food.ordering.system.order.service.domain.ports.output.message.publisher.payment.OrderCancelledPaymentRequestMessagePublisher;
import com.food.ordering.system.order.service.domain.saga.OrderApprovalSaga;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/***
 Belum diimplement, nanti akan diterapkan SAGA pattern di akhir.

 Ingat, firing event dan persist database seharusnya dilakukan atomic.
 ***/


@Slf4j
@Validated
@Service
public class RestaurantResponseMessageListenerImpl implements RestaurantApprovalResponseMessageListener {

    private final OrderApprovalSaga orderApprovalSaga;
    private final OrderCancelledPaymentRequestMessagePublisher orderCancelledPaymentRequestMessagePublisher;

    @Autowired
    public RestaurantResponseMessageListenerImpl(OrderApprovalSaga orderApprovalSaga,
                                                 OrderCancelledPaymentRequestMessagePublisher orderCancelledPaymentRequestMessagePublisher) {
        this.orderApprovalSaga = orderApprovalSaga;
        this.orderCancelledPaymentRequestMessagePublisher = orderCancelledPaymentRequestMessagePublisher;
    }

    @Override
    public void orderApproved(RestaurantApprovalResponse restaurantApprovalResponse) {
        orderApprovalSaga.process(restaurantApprovalResponse);
        log.info("Restaurant approved the order with ID : {} and will process it!",restaurantApprovalResponse.getOrderId());
    }

    @Override
    public void orderRejected(RestaurantApprovalResponse restaurantApprovalResponse) {
        OrderCancelledEvent orderCancelledEvent = orderApprovalSaga.rollback(restaurantApprovalResponse);
        log.info("Publishing OrderCancelledEvent with Order ID : {} with failure messages {}",
                restaurantApprovalResponse.getOrderId(),
                String.join("\n",restaurantApprovalResponse.getFailureMessages()));
        orderCancelledPaymentRequestMessagePublisher.publish(orderCancelledEvent);
    }
}
