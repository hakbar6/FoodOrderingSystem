package com.food.ordering.system.restaurant.service.messaging.publisher.kafka;

import com.food.ordering.system.kafka.order.avro.model.RestaurantApprovalResponseAvroModel;
import com.food.ordering.system.kafka.producer.helper.KafkaMessageHelper;
import com.food.ordering.system.kafka.producer.service.KafkaProducer;
import com.food.ordering.system.restaurant.service.domain.config.RestaurantServiceConfigData;
import com.food.ordering.system.restaurant.service.domain.event.OrderApproveEvent;
import com.food.ordering.system.restaurant.service.domain.ports.output.message.publisher.OrderApprovedMessagePublisher;
import com.food.ordering.system.restaurant.service.messaging.mapper.RestaurantMessageDataMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderApprovedKafkaMessagePublisher implements OrderApprovedMessagePublisher {

    private final RestaurantMessageDataMapper dataMapper;
    private final RestaurantServiceConfigData restaurantServiceConfigData;
    private final KafkaProducer<String, RestaurantApprovalResponseAvroModel> kafkaProducer;
    private final KafkaMessageHelper kafkaMessageHelper;

    @Autowired
    public OrderApprovedKafkaMessagePublisher(RestaurantMessageDataMapper dataMapper,
                                              RestaurantServiceConfigData restaurantServiceConfigData,
                                              KafkaProducer<String, RestaurantApprovalResponseAvroModel> kafkaProducer,
                                              KafkaMessageHelper kafkaMessageHelper) {
        this.dataMapper = dataMapper;
        this.restaurantServiceConfigData = restaurantServiceConfigData;
        this.kafkaProducer = kafkaProducer;
        this.kafkaMessageHelper = kafkaMessageHelper;
    }

    @Override
    public void publish(OrderApproveEvent domainEvent) {
        String orderId = domainEvent.getOrderApproval().getOrderID().toString();

        log.info("Received Restaurant Approve Event for order id : {}",orderId);

        try {
            RestaurantApprovalResponseAvroModel restaurantApprovalResponseAvroModel =
                    dataMapper.orderApproveEventToRestaurantApprovalResponseAvroModel(domainEvent);

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
            log.error("Error while sending RestaurantApprovalResponse message from completed event" +
                    " to kafka with order id: {}, error: {}", orderId, e.getMessage());
        }
    }
}
