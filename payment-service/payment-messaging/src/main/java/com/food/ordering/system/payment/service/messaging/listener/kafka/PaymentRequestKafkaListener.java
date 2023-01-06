package com.food.ordering.system.payment.service.messaging.listener.kafka;

import com.food.ordering.system.kafka.consumer.KafkaConsumer;
import com.food.ordering.system.kafka.order.avro.model.PaymentOrderStatus;
import com.food.ordering.system.kafka.order.avro.model.PaymentRequestAvroModel;
import com.food.ordering.system.payment.service.domain.ports.input.message.listener.PaymentRequestMessageListener;
import com.food.ordering.system.payment.service.messaging.mapper.PaymentMessagingDataMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class PaymentRequestKafkaListener implements KafkaConsumer<PaymentRequestAvroModel> {

    private final PaymentRequestMessageListener paymentRequestMessageListener;
    private final PaymentMessagingDataMapper dataMapper;

    @Autowired
    public PaymentRequestKafkaListener(PaymentRequestMessageListener paymentRequestMessageListener,
                                       PaymentMessagingDataMapper dataMapper) {
        this.paymentRequestMessageListener = paymentRequestMessageListener;
        this.dataMapper = dataMapper;
    }

    @Override
    @KafkaListener(id = "${kafka-consumer-config.payment-consumer-group-id}",
                    topics = "${payment-service.payment-request-topic-name}")
    public void receive(@Payload List<PaymentRequestAvroModel> messages,
                        @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) List<String> keys,
                        @Header(KafkaHeaders.RECEIVED_PARTITION_ID) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets) {
        log.info("{} number of payment requests received with keys:{}, partitions:{} and offsets: {}",
                messages.size(),
                keys.toString(),
                partitions.toString(),
                offsets.toString());

        messages.forEach(paymentRequestAvroModel -> {
            if (paymentRequestAvroModel.getPaymentOrderStatus() ==  PaymentOrderStatus.PENDING) {
                log.info("Processing Payment for order id : {}",paymentRequestAvroModel.getOrderId());
                paymentRequestMessageListener.completePayment(
                        dataMapper.paymentRequestAvroModelToPaymentRequest(paymentRequestAvroModel)
                );
            } else if (paymentRequestAvroModel.getPaymentOrderStatus() ==  PaymentOrderStatus.CANCELLED) {
                log.info("Cancelling Payment for order id : {}",paymentRequestAvroModel.getOrderId());
                paymentRequestMessageListener.cancelPayment(
                        dataMapper.paymentRequestAvroModelToPaymentRequest(paymentRequestAvroModel)
                );
            }
        });
    }
}
