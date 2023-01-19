package com.food.ordering.system.order.service.application.rest;

import com.food.ordering.system.order.service.domain.dto.request.CreateOrderRequest;
import com.food.ordering.system.order.service.domain.dto.request.CreateOrderResponse;
import com.food.ordering.system.order.service.domain.dto.track.TrackOrderQuery;
import com.food.ordering.system.order.service.domain.dto.track.TrackOrderResponse;
import com.food.ordering.system.order.service.domain.ports.input.service.OrderApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(value = "/orders", produces = "application/vnd.api.v1+json")
public class OrderController {
    private final OrderApplicationService orderApplicationService;

    @Autowired
    public OrderController(OrderApplicationService orderApplicationService) {
        this.orderApplicationService = orderApplicationService;
    }

    @PostMapping()
    public ResponseEntity<CreateOrderResponse> createOrder(
            @RequestBody CreateOrderRequest createOrderRequest
    ){
        log.info("Creating order for customer {} at restaurant {}",createOrderRequest.getCustomerId(),
                createOrderRequest.getRestaurantId());
        CreateOrderResponse orderResponse = orderApplicationService.createOrder(createOrderRequest);
        log.info("Order created with tracking id : {}",orderResponse.getTrackingId());
        return ResponseEntity.ok(orderResponse);
    }

    @GetMapping(value = "/{trackingId}")
    public ResponseEntity<TrackOrderResponse> getOrderByTrackingID(@PathVariable UUID trackingId) {
        TrackOrderResponse trackOrderResponse =
                orderApplicationService.trackOrder(TrackOrderQuery.builder().orderTrackingID(trackingId).build());
        log.info("Returning order status with tracking ID : {}", trackOrderResponse.getOrderTrackingId());
        return ResponseEntity.ok(trackOrderResponse);
    }
}
