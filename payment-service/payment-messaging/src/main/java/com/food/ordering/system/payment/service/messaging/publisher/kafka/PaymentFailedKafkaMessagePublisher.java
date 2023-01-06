package com.food.ordering.system.payment.service.messaging.publisher.kafka;

import com.food.ordering.system.kafka.order.avro.model.PaymentResponseAvroModel;
import com.food.ordering.system.kafka.producer.helper.KafkaMessageHelper;
import com.food.ordering.system.kafka.producer.service.KafkaProducer;
import com.food.ordering.system.payment.service.domain.config.PaymentServiceConfigData;
import com.food.ordering.system.payment.service.domain.event.PaymentFailedEvent;
import com.food.ordering.system.payment.service.domain.ports.output.message.publisher.PaymentFailedMessagePublisher;
import com.food.ordering.system.payment.service.messaging.mapper.PaymentMessagingDataMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PaymentFailedKafkaMessagePublisher implements PaymentFailedMessagePublisher {

    private final PaymentMessagingDataMapper dataMapper;
    private final PaymentServiceConfigData paymentServiceConfigData;
    private final KafkaProducer<String, PaymentResponseAvroModel> kafkaProducer;
    private final KafkaMessageHelper kafkaMessageHelper;

    @Autowired
    public PaymentFailedKafkaMessagePublisher(PaymentMessagingDataMapper dataMapper,
                                              PaymentServiceConfigData paymentServiceConfigData,
                                              KafkaProducer<String, PaymentResponseAvroModel> kafkaProducer,
                                              KafkaMessageHelper kafkaMessageHelper) {
        this.dataMapper = dataMapper;
        this.paymentServiceConfigData = paymentServiceConfigData;
        this.kafkaProducer = kafkaProducer;
        this.kafkaMessageHelper = kafkaMessageHelper;
    }

    @Override
    public void publish(PaymentFailedEvent domainEvent) {
        String orderId = domainEvent.getPayment().getOrderID().getValue().toString();

        log.info("Received PaymentFailedEvent for order id : {}",orderId);

        try {
            PaymentResponseAvroModel paymentResponseAvroModel = dataMapper
                    .paymentFailedEventToPaymentResponseAvroModel(domainEvent);

            kafkaProducer.send(
                    paymentServiceConfigData.getPaymentResponseTopicName(),
                    orderId,
                    paymentResponseAvroModel,
                    kafkaMessageHelper.getKafkaCallback(
                            paymentServiceConfigData.getPaymentResponseTopicName(),
                            paymentResponseAvroModel,
                            orderId,
                            "PaymentResponseAvroModel"
                    )
            );
        } catch (Exception e){
            log.error("Error while sending PaymentResponseAvroModel message from failed event" +
                    " to kafka with order id: {}, error: {}", orderId, e.getMessage());
        }
    }
}
