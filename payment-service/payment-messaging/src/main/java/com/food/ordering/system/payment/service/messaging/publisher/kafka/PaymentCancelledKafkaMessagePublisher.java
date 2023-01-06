package com.food.ordering.system.payment.service.messaging.publisher.kafka;

import com.food.ordering.system.kafka.order.avro.model.PaymentResponseAvroModel;
import com.food.ordering.system.kafka.producer.helper.KafkaMessageHelper;
import com.food.ordering.system.kafka.producer.service.KafkaProducer;
import com.food.ordering.system.payment.service.domain.config.PaymentServiceConfigData;
import com.food.ordering.system.payment.service.domain.event.PaymentCancelledEvent;
import com.food.ordering.system.payment.service.domain.ports.output.message.publisher.PaymentCancelledMessagePublisher;
import com.food.ordering.system.payment.service.messaging.mapper.PaymentMessagingDataMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PaymentCancelledKafkaMessagePublisher implements PaymentCancelledMessagePublisher {

    private final KafkaProducer<String, PaymentResponseAvroModel> kafkaProducer;
    private final KafkaMessageHelper messageHelper;
    private final PaymentServiceConfigData paymentServiceConfigData;
    private final PaymentMessagingDataMapper dataMapper;

    @Autowired
    public PaymentCancelledKafkaMessagePublisher(KafkaProducer<String, PaymentResponseAvroModel> kafkaProducer,
                                                 KafkaMessageHelper messageHelper,
                                                 PaymentServiceConfigData paymentServiceConfigData,
                                                 PaymentMessagingDataMapper dataMapper) {
        this.kafkaProducer = kafkaProducer;
        this.messageHelper = messageHelper;
        this.paymentServiceConfigData = paymentServiceConfigData;
        this.dataMapper = dataMapper;
    }

    @Override
    public void publish(PaymentCancelledEvent domainEvent) {
        String orderId = domainEvent.getPayment().getOrderID().getValue().toString();

        log.info("Received Payment Cancelled event for Order Id : {}", orderId);

        try {
            PaymentResponseAvroModel paymentResponseAvroModel = dataMapper
                    .paymentCancelledEventToPaymentResponseAvroModel(domainEvent);

            kafkaProducer.send(
                    paymentServiceConfigData.getPaymentResponseTopicName(),
                    orderId,
                    paymentResponseAvroModel,
                    messageHelper.getKafkaCallback(
                            paymentServiceConfigData.getPaymentResponseTopicName(),
                            paymentResponseAvroModel,
                            orderId,
                            "PaymentResponseAvroModel"
                    )
            );
        } catch (Exception e) {
            log.error("Error while sending PaymentResponseAvroModel message from cancelled event" +
                    " to kafka with order id: {}, error: {}", orderId, e.getMessage());
        }
    }
}
