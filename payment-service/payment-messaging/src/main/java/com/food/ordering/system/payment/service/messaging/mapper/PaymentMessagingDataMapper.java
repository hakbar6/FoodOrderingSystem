package com.food.ordering.system.payment.service.messaging.mapper;

import com.food.ordering.system.domain.valueobject.PaymentOrderStatus;
import com.food.ordering.system.kafka.order.avro.model.PaymentRequestAvroModel;
import com.food.ordering.system.kafka.order.avro.model.PaymentResponseAvroModel;
import com.food.ordering.system.kafka.order.avro.model.PaymentStatus;
import com.food.ordering.system.payment.service.domain.dto.PaymentRequest;
import com.food.ordering.system.payment.service.domain.event.PaymentCancelledEvent;
import com.food.ordering.system.payment.service.domain.event.PaymentCompletedEvent;
import com.food.ordering.system.payment.service.domain.event.PaymentFailedEvent;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PaymentMessagingDataMapper {

    public PaymentResponseAvroModel paymentCompletedEventToPaymentResponseAvroModel(PaymentCompletedEvent paymentCompletedEvent) {
        return PaymentResponseAvroModel.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setSagaId("") // nanti akan di set, ketika implementasi saga pattern
                .setPaymentId(paymentCompletedEvent.getPayment().getId().getValue().toString())
                .setCustomerId(paymentCompletedEvent.getPayment().getCustomerID().getValue().toString())
                .setOrderId(paymentCompletedEvent.getPayment().getOrderID().getValue().toString())
                .setPrice(paymentCompletedEvent.getPayment().getPrice().getAmount())
                .setCreatedAt(paymentCompletedEvent.getCreatedAt().toInstant())
                .setPaymentStatus(PaymentStatus.valueOf(paymentCompletedEvent.getPayment().getPaymentStatus().toString()))
                .setFailureMessages(paymentCompletedEvent.getFailureMessages())
                .build();
    }

    public PaymentResponseAvroModel paymentCancelledEventToPaymentResponseAvroModel(PaymentCancelledEvent paymentCancelledEvent){
        return PaymentResponseAvroModel.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setSagaId("") // nanti akan di set, ketika implementasi saga pattern
                .setPaymentId(paymentCancelledEvent.getPayment().getId().getValue().toString())
                .setCustomerId(paymentCancelledEvent.getPayment().getCustomerID().getValue().toString())
                .setOrderId(paymentCancelledEvent.getPayment().getOrderID().getValue().toString())
                .setPrice(paymentCancelledEvent.getPayment().getPrice().getAmount())
                .setCreatedAt(paymentCancelledEvent.getCreatedAt().toInstant())
                .setPaymentStatus(PaymentStatus.valueOf(paymentCancelledEvent.getPayment().getPaymentStatus().toString()))
                .setFailureMessages(paymentCancelledEvent.getFailureMessages())
                .build();
    }

    public PaymentResponseAvroModel paymentFailedEventToPaymentResponseAvroModel(PaymentFailedEvent paymentFailedEvent){
        return PaymentResponseAvroModel.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setSagaId("") // nanti akan di set, ketika implementasi saga pattern
                .setPaymentId(paymentFailedEvent.getPayment().getId().getValue().toString())
                .setCustomerId(paymentFailedEvent.getPayment().getCustomerID().getValue().toString())
                .setOrderId(paymentFailedEvent.getPayment().getOrderID().getValue().toString())
                .setPrice(paymentFailedEvent.getPayment().getPrice().getAmount())
                .setCreatedAt(paymentFailedEvent.getCreatedAt().toInstant())
                .setPaymentStatus(PaymentStatus.valueOf(paymentFailedEvent.getPayment().getPaymentStatus().toString()))
                .setFailureMessages(paymentFailedEvent.getFailureMessages())
                .build();
    }

    public PaymentRequest paymentRequestAvroModelToPaymentRequest(PaymentRequestAvroModel paymentRequestAvroModel) {
        return PaymentRequest.builder()
                .id(paymentRequestAvroModel.getId())
                .sagaId(paymentRequestAvroModel.getSagaId())
                .customerId(paymentRequestAvroModel.getCustomerId())
                .orderId(paymentRequestAvroModel.getOrderId())
                .price(paymentRequestAvroModel.getPrice())
                .createdAt(paymentRequestAvroModel.getCreatedAt())
                .paymentOrderStatus(PaymentOrderStatus.valueOf(paymentRequestAvroModel.getPaymentOrderStatus().toString()))
                .build();
    }
}
