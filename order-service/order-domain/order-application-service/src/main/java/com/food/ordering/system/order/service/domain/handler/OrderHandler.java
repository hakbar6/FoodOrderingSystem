package com.food.ordering.system.order.service.domain.handler;

import com.food.ordering.system.order.service.domain.OrderDomainService;
import com.food.ordering.system.order.service.domain.dto.create.CreateOrderRequest;
import com.food.ordering.system.order.service.domain.dto.create.CreateOrderResponse;
import com.food.ordering.system.order.service.domain.entity.orderProcessingAggregate.Order;
import com.food.ordering.system.order.service.domain.entity.restaurantAggregate.Restaurant;
import com.food.ordering.system.order.service.domain.event.OrderCreatedEvent;
import com.food.ordering.system.order.service.domain.execption.OrderDomainException;
import com.food.ordering.system.order.service.domain.mapper.OrderDataMapper;
import com.food.ordering.system.order.service.domain.ports.output.message.publisher.payment.OrderCreatedPaymentRequestMessagePublisher;
import com.food.ordering.system.order.service.domain.ports.output.repository.CustomerRepository;
import com.food.ordering.system.order.service.domain.ports.output.repository.OrderRepository;
import com.food.ordering.system.order.service.domain.ports.output.repository.RestaurantRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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
        return mapper.orderEntityToCreateOrderResponse(result.getOrder(),"SUCESS");
    }
}
