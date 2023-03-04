package com.food.ordering.system.order.service.domain.handler;

import com.food.ordering.system.order.service.domain.dto.request.CreateOrderRequest;
import com.food.ordering.system.order.service.domain.dto.response.CreateOrderResponse;
import com.food.ordering.system.order.service.domain.event.OrderCreatedEvent;
import com.food.ordering.system.order.service.domain.helper.OrderHelper;
import com.food.ordering.system.order.service.domain.mapper.OrderDataMapper;
import com.food.ordering.system.order.service.domain.ports.output.message.publisher.payment.OrderCreatedPaymentRequestMessagePublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderHandler {
    private final OrderHelper orderHelper;
    private final OrderDataMapper mapper;
    private final OrderCreatedPaymentRequestMessagePublisher createdPaymentRequestMessagePublisher;

    @Autowired
    public OrderHandler(OrderHelper orderHelper,
                        OrderDataMapper mapper,
                        OrderCreatedPaymentRequestMessagePublisher createdPaymentRequestMessagePublisher) {
        this.orderHelper = orderHelper;
        this.mapper = mapper;
        this.createdPaymentRequestMessagePublisher = createdPaymentRequestMessagePublisher;
    }

    public CreateOrderResponse createOrder(CreateOrderRequest request) {
        OrderCreatedEvent result = orderHelper.persistOrder(request);
        log.info("Success Create Order with Order ID {}",result.getOrder().getId().getValue());
        createdPaymentRequestMessagePublisher.publish(result);
        return mapper.orderDomainEntityToCreateOrderResponse(result.getOrder(),"SUCCESS");
    }
}
