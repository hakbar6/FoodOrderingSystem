package com.food.ordering.system.order.service.domain.handler;


import com.food.ordering.system.order.service.domain.dto.track.TrackOrderQuery;
import com.food.ordering.system.order.service.domain.dto.track.TrackOrderResponse;
import com.food.ordering.system.order.service.domain.entity.orderProcessingAggregate.Order;
import com.food.ordering.system.order.service.domain.execption.OrderNotFoundException;
import com.food.ordering.system.order.service.domain.mapper.OrderDataMapper;
import com.food.ordering.system.order.service.domain.ports.output.repository.OrderRepository;
import com.food.ordering.system.order.service.domain.valueobjects.TrackingId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Component
public class TrackHandler {
    private final OrderDataMapper mapper;
    private final OrderRepository orderRepository;

    @Autowired
    public TrackHandler(OrderDataMapper mapper, OrderRepository orderRepository) {
        this.mapper = mapper;
        this.orderRepository = orderRepository;
    }

    @Transactional(readOnly = true)
    public TrackOrderResponse trackOrder(TrackOrderQuery query) {
        Optional<Order> result = orderRepository.findOrderByTrackingId(
                new TrackingId(query.getOrderTrackingID())
        );
        if (result.isEmpty()) {
            log.warn("Could not found order with Tracking ID : {}",query.getOrderTrackingID());
            throw new OrderNotFoundException("Could not found order with Tracking ID : " + query.getOrderTrackingID());
        }
        return mapper.orderEntityToTrackOrderResponse(result.get());
    }
}
