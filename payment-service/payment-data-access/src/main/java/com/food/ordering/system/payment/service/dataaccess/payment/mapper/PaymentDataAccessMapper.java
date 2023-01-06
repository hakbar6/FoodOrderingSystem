package com.food.ordering.system.payment.service.dataaccess.payment.mapper;


import com.food.ordering.system.domain.valueobject.CustomerID;
import com.food.ordering.system.domain.valueobject.Money;
import com.food.ordering.system.domain.valueobject.OrderID;
import com.food.ordering.system.payment.service.dataaccess.payment.entity.PaymentEntity;
import com.food.ordering.system.payment.service.domain.entity.paymentProcessingAggregate.Payment;
import com.food.ordering.system.payment.service.domain.valueobjects.PaymentId;
import org.springframework.stereotype.Component;

@Component
public class PaymentDataAccessMapper {
    public PaymentEntity paymentToPaymentEntity(Payment payment) {
        return PaymentEntity.builder()
                .id(payment.getId().getValue())
                .customerId(payment.getCustomerID().getValue())
                .orderId(payment.getOrderID().getValue())
                .price(payment.getPrice().getAmount())
                .status(payment.getPaymentStatus())
                .createdAt(payment.getCreatedAt())
                .build();
    }

    public Payment paymentEntityToPayment(PaymentEntity paymentEntity) {
        return Payment.builder()
                .paymentId(new PaymentId(paymentEntity.getId()))
                .customerID(new CustomerID(paymentEntity.getCustomerId()))
                .orderID(new OrderID(paymentEntity.getOrderId()))
                .price(new Money(paymentEntity.getPrice()))
                .createdAt(paymentEntity.getCreatedAt())
                .build();
    }
}
