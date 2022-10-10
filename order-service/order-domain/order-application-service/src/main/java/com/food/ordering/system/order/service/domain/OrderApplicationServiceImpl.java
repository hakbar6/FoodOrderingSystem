package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.order.service.domain.dto.create.CreateOrderRequest;
import com.food.ordering.system.order.service.domain.dto.create.CreateOrderResponse;
import com.food.ordering.system.order.service.domain.dto.track.TrackOrderQuery;
import com.food.ordering.system.order.service.domain.dto.track.TrackOrderResponse;
import com.food.ordering.system.order.service.domain.handler.OrderHandler;
import com.food.ordering.system.order.service.domain.handler.TrackHandler;
import com.food.ordering.system.order.service.domain.ports.input.service.OrderApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Validated // anotasi validated digunakan untuk menjalankan validation yang telah kita buat pada interfacenya
@Service
class OrderApplicationServiceImpl implements OrderApplicationService {
    private final OrderHandler orderHandler;
    private final TrackHandler trackHandler;

    @Autowired
    public OrderApplicationServiceImpl(OrderHandler orderHandler, TrackHandler trackHandler) {
        this.orderHandler = orderHandler;
        this.trackHandler = trackHandler;
    }

    @Override
    public CreateOrderResponse createOrder(CreateOrderRequest request) {
        return orderHandler.createOrder(request);
    }

    @Override
    public TrackOrderResponse trackOrder(TrackOrderQuery request) {
        return trackHandler.trackOrder(request);
    }
}
