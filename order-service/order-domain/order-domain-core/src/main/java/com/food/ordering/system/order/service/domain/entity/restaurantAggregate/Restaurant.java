package com.food.ordering.system.order.service.domain.entity.restaurantAggregate;

import com.food.ordering.system.domain.entity.AggregateRoot;
import com.food.ordering.system.domain.valueobject.ProductID;
import com.food.ordering.system.domain.valueobject.RestaurantID;
import com.food.ordering.system.order.service.domain.entity.orderProcessingAggregate.Product;

import java.util.Map;


public class Restaurant extends AggregateRoot<RestaurantID> {
    private final Map<ProductID, Product> products;

    private boolean active;

    private Restaurant(Builder builder) {
        super.setId(builder.restaurantID);
        products = builder.products;
        active = builder.active;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Map<ProductID, Product> getProducts() {
        return products;
    }

    public boolean isActive() {
        return active;
    }


    public static final class Builder {
        private RestaurantID restaurantID;
        private Map<ProductID, Product> products;
        private boolean active;

        private Builder() {
        }

        public Builder id(RestaurantID val) {
            restaurantID = val;
            return this;
        }

        public Builder products(Map<ProductID, Product> val) {
            products = val;
            return this;
        }

        public Builder active(boolean val) {
            active = val;
            return this;
        }

        public Restaurant build() {
            return new Restaurant(this);
        }
    }
}
