package com.food.ordering.system.order.service.domain.entity.restaurantAggregate;

import com.food.ordering.system.domain.entity.AggregateRoot;
import com.food.ordering.system.domain.valueobject.RestaurantID;
import com.food.ordering.system.order.service.domain.entity.orderProcessingAggregate.Product;

import java.util.List;

public class Restaurant extends AggregateRoot<RestaurantID> {
    private final List<Product> products;

    private boolean active;

    private Restaurant(Builder builder) {
        super.setId(builder.restaurantID);
        products = builder.products;
        active = builder.active;
    }

    public static Builder builder() {
        return new Builder();
    }

    public List<Product> getListProducts() {
        return this.products;
    }

    public boolean isActive() {
        return this.active;
    }


    public static final class Builder {
        private RestaurantID restaurantID;
        private List<Product> products;
        private boolean active;

        private Builder() {
        }

        public Builder id(RestaurantID val) {
            restaurantID = val;
            return this;
        }

        public Builder products(List<Product> val) {
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
