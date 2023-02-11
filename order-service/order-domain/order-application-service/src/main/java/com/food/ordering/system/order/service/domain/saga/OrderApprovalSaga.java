package com.food.ordering.system.order.service.domain.saga;

import com.food.ordering.system.domain.eventdomain.EmptyEvent;
import com.food.ordering.system.order.service.domain.OrderDomainService;
import com.food.ordering.system.order.service.domain.dto.response.RestaurantApprovalResponse;
import com.food.ordering.system.order.service.domain.entity.orderProcessingAggregate.Order;
import com.food.ordering.system.order.service.domain.event.OrderCancelledEvent;
import com.food.ordering.system.order.service.domain.helper.OrderSagaHelper;
import com.food.ordering.system.saga.SagaStep;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/*
 jika respon restaurant sukses, maka proses dan tidak mengembalikan event apapun
 jika respon restaurant gagal, perlu menerbitkan event orderCancelledEvent untuk memicu
 proses cancelling payment pada sisi payment service
*/

@Slf4j
@Component
public class OrderApprovalSaga implements SagaStep<RestaurantApprovalResponse, EmptyEvent, OrderCancelledEvent> {

    private final OrderDomainService orderDomainService;
    private final OrderSagaHelper orderSagaHelper;

    @Autowired
    public OrderApprovalSaga(OrderDomainService orderDomainService, OrderSagaHelper orderSagaHelper) {
        this.orderDomainService = orderDomainService;
        this.orderSagaHelper = orderSagaHelper;
    }

    @Override
    public EmptyEvent process(RestaurantApprovalResponse data) {
        log.info("Approving order with ID : {} ", data.getOrderId());
        Order order = orderSagaHelper.findOrder(data.getOrderId());
        orderDomainService.approveOrder(order);
        orderSagaHelper.saveOrder(order);
        log.info("Order with ID : {} is approved!", order.getId().getValue());
        return EmptyEvent.INSTANCE;
    }

    @Override
    public OrderCancelledEvent rollback(RestaurantApprovalResponse data) {
        log.info("Cancelling order with ID : {} ",data.getOrderId());
        Order order = orderSagaHelper.findOrder(data.getOrderId());
        OrderCancelledEvent orderCancelledEvent = orderDomainService.cancellingOrderPayment(order, data.getFailureMessages());
        orderSagaHelper.saveOrder(order);
        log.info("Order with ID : {} is cancelling", order.getId().getValue());
        return orderCancelledEvent;
    }
}
