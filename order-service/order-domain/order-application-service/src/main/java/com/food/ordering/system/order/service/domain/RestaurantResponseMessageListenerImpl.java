package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.order.service.domain.dto.response.RestaurantApprovalResponse;
import com.food.ordering.system.order.service.domain.ports.input.message.listener.restaurant.RestaurantApprovalResponseMessageListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/***
 Belum diimplement, nanti akan diterapkan SAGA pattern di akhir.

 Ingat, firing event dan persist database seharusnya dilakukan atomic.
 ***/


@Slf4j
@Validated
@Service
public class RestaurantResponseMessageListenerImpl implements RestaurantApprovalResponseMessageListener {
    @Override
    public void orderApproved(RestaurantApprovalResponse restaurantApprovalResponse) {

    }

    @Override
    public void orderRejected(RestaurantApprovalResponse restaurantApprovalResponse) {

    }
}
