package com.food.ordering.system.order.service.domain.dto.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class OrderItemRequest {
    @NotNull
    private final UUID productId;
    @NotNull
    private final BigDecimal price;
    @NotNull
    private final Integer quantity;
    @NotNull
    private final BigDecimal subTotal;
}
