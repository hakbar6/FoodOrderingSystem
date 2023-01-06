package com.food.ordering.system.payment.service.domain.mapper;

import com.food.ordering.system.domain.valueobject.CustomerID;
import com.food.ordering.system.domain.valueobject.Money;
import com.food.ordering.system.domain.valueobject.OrderID;
import com.food.ordering.system.payment.service.domain.dto.PaymentRequest;
import com.food.ordering.system.payment.service.domain.entity.paymentProcessingAggregate.Payment;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PaymentDataMapper {
    public Payment paymentRequestToPaymentDomain(PaymentRequest paymentRequest) {
        return Payment.builder()
                .orderID(new OrderID(UUID.fromString(paymentRequest.getOrderId())))
                .customerID(new CustomerID(UUID.fromString(paymentRequest.getCustomerId())))
                .price(new Money(paymentRequest.getPrice()))
                .build();
    }
}
