package com.food.ordering.system.restaurant.service.domain.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductRequest {
    private String productId;
    private int quantity;
}
