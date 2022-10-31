package com.food.ordering.system.domain.valueobject;

import java.util.UUID;

public class RestaurantID extends BaseId<UUID> {
    public RestaurantID(UUID value) {
        super(value);
    }
}
