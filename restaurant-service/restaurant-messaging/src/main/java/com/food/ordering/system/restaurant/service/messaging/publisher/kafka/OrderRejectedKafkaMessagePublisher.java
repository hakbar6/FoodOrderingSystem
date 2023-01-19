package com.food.ordering.system.restaurant.service.messaging.publisher.kafka;

import com.food.ordering.system.kafka.order.avro.model.RestaurantApprovalResponseAvroModel;
import com.food.ordering.system.kafka.producer.helper.KafkaMessageHelper;
import com.food.ordering.system.kafka.producer.service.KafkaProducer;
import com.food.ordering.system.restaurant.service.domain.config.RestaurantServiceConfigData;
import com.food.ordering.system.restaurant.service.domain.event.OrderRejectedEvent;
import com.food.ordering.system.restaurant.service.domain.ports.output.message.publisher.OrderRejectedMessagePublisher;
import com.food.ordering.system.restaurant.service.messaging.mapper.RestaurantMessageDataMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderRejectedKafkaMessagePublisher implements OrderRejectedMessagePublisher {

    private final RestaurantMessageDataMapper dataMapper;
    private final RestaurantServiceConfigData restaurantServiceConfigData;
    private final KafkaProducer<String, RestaurantApprovalResponseAvroModel> kafkaProducer;
    private final KafkaMessageHelper kafkaMessageHelper;

    public OrderRejectedKafkaMessagePublisher(RestaurantMessageDataMapper dataMapper,
                                              RestaurantServiceConfigData restaurantServiceConfigData,
                                              KafkaProducer<String, RestaurantApprovalResponseAvroModel> kafkaProducer,
                                              KafkaMessageHelper kafkaMessageHelper) {
        this.dataMapper = dataMapper;
        this.restaurantServiceConfigData = restaurantServiceConfigData;
        this.kafkaProducer = kafkaProducer;
        this.kafkaMessageHelper = kafkaMessageHelper;
    }

    @Override
    public void publish(OrderRejectedEvent domainEvent) {
        String orderId = domainEvent.getOrderApproval().getOrderID().toString();

        log.info("Received Restaurant Rejected Event for order id : {}",orderId);

        try {
            RestaurantApprovalResponseAvroModel restaurantApprovalResponseAvroModel =
                    dataMapper.orderRejectedEventToRestaurantApprovalResponseAvroModel(domainEvent);

            kafkaProducer.send(
                    restaurantServiceConfigData.getRestaurantApprovalResponseTopicName(),
                    orderId,
                    restaurantApprovalResponseAvroModel,
                    kafkaMessageHelper.getKafkaCallback(
                            restaurantServiceConfigData.getRestaurantApprovalResponseTopicName(),
                            restaurantApprovalResponseAvroModel,
                            orderId,
                            "RestaurantApprovalResponseAvroModel"
                    )
            );
        } catch (Exception e) {
            log.error("Error while sending RestaurantApprovalResponse message from rejected event" +
                    " to kafka with order id: {}, error: {}", orderId, e.getMessage());
        }
    }
}
