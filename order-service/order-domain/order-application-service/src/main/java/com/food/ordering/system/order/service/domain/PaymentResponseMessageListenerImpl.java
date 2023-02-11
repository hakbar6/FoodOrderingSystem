package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.order.service.domain.dto.response.PaymentResponse;
import com.food.ordering.system.order.service.domain.event.OrderPaidEvent;
import com.food.ordering.system.order.service.domain.ports.input.message.listener.payment.PaymentResponseMessageListener;
import com.food.ordering.system.order.service.domain.ports.output.message.publisher.restaurant.OrderPaidRestaurantRequestMessagePublisher;
import com.food.ordering.system.order.service.domain.saga.OrderPaymentSaga;
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
public class PaymentResponseMessageListenerImpl implements PaymentResponseMessageListener {

    private final OrderPaymentSaga orderPaymentSaga;
    private final OrderPaidRestaurantRequestMessagePublisher orderPaidRestaurantRequestMessagePublisher;

    @Autowired
    public PaymentResponseMessageListenerImpl(OrderPaymentSaga orderPaymentSaga,
                                              OrderPaidRestaurantRequestMessagePublisher orderPaidRestaurantRequestMessagePublisher) {
        this.orderPaymentSaga = orderPaymentSaga;
        this.orderPaidRestaurantRequestMessagePublisher = orderPaidRestaurantRequestMessagePublisher;
    }

    @Override
    public void paymentCompleted(PaymentResponse paymentResponse) {
        OrderPaidEvent orderPaidEvent = orderPaymentSaga.process(paymentResponse);
        log.info("Publishing OrderPaidEvent for order ID {}", orderPaidEvent.getOrder().getId().getValue());
        orderPaidRestaurantRequestMessagePublisher.publish(orderPaidEvent);
    }

    @Override
    public void paymentCancelled(PaymentResponse paymentResponse) {
        orderPaymentSaga.rollback(paymentResponse);
        log.info("Order is roll backed for order id : {} with failure messages {}",
                paymentResponse.getOrderId(),
                String.join("\n",paymentResponse.getFailureMessages()));
    }
}
