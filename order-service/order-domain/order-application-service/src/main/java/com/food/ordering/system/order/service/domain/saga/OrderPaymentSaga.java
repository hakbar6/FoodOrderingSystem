package com.food.ordering.system.order.service.domain.saga;

import com.food.ordering.system.domain.eventdomain.EmptyEvent;
import com.food.ordering.system.domain.valueobject.OrderID;
import com.food.ordering.system.order.service.domain.OrderDomainService;
import com.food.ordering.system.order.service.domain.dto.response.PaymentResponse;
import com.food.ordering.system.order.service.domain.entity.orderProcessingAggregate.Order;
import com.food.ordering.system.order.service.domain.event.OrderPaidEvent;
import com.food.ordering.system.order.service.domain.execption.OrderNotFoundException;
import com.food.ordering.system.order.service.domain.helper.OrderSagaHelper;
import com.food.ordering.system.order.service.domain.ports.output.repository.OrderRepository;
import com.food.ordering.system.saga.SagaStep;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class OrderPaymentSaga implements SagaStep<PaymentResponse, OrderPaidEvent, EmptyEvent> {

    private final OrderDomainService orderDomainService;
    private final OrderSagaHelper orderSagaHelper;

    @Autowired
    public OrderPaymentSaga(OrderDomainService orderDomainService, OrderSagaHelper orderSagaHelper) {
        this.orderDomainService = orderDomainService;
        this.orderSagaHelper = orderSagaHelper;
    }

    @Override
    @Transactional
    public OrderPaidEvent process(PaymentResponse data) {
        log.info("Completing payment for order with id {}", data.getOrderId());
        Order order = orderSagaHelper.findOrder(data.getOrderId());
        OrderPaidEvent domainEvent = orderDomainService.payOrder(order);
        orderSagaHelper.saveOrder(order);
        log.info("Order with ID {} is paid!", order.getId().getValue());
        return domainEvent;
    }

    @Override
    @Transactional
    public EmptyEvent rollback(PaymentResponse data) {
        log.info("Cancelling order with id {} ",data.getOrderId());
        Order order = orderSagaHelper.findOrder(data.getOrderId());
        orderDomainService.cancelOrder(order, data.getFailureMessages());
        orderSagaHelper.saveOrder(order);
        log.info("Order with id {} is cancelled",order.getId().getValue());
        return EmptyEvent.INSTANCE;
    }
}
