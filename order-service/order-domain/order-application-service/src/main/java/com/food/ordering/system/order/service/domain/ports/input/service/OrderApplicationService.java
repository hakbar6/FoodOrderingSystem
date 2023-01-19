package com.food.ordering.system.order.service.domain.ports.input.service;


import com.food.ordering.system.order.service.domain.dto.request.CreateOrderRequest;
import com.food.ordering.system.order.service.domain.dto.request.CreateOrderResponse;
import com.food.ordering.system.order.service.domain.dto.track.TrackOrderQuery;
import com.food.ordering.system.order.service.domain.dto.track.TrackOrderResponse;

import javax.validation.Valid;

public interface OrderApplicationService {
    CreateOrderResponse createOrder(@Valid CreateOrderRequest request);

    TrackOrderResponse trackOrder(@Valid TrackOrderQuery request);
}
