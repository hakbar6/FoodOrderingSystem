package com.food.ordering.system.restaurant.service.domain.entity;

import com.food.ordering.system.domain.entity.BaseEntity;
import com.food.ordering.system.domain.valueobject.Money;
import com.food.ordering.system.domain.valueobject.OrderID;
import com.food.ordering.system.domain.valueobject.OrderStatus;
import com.food.ordering.system.domain.valueobject.ProductID;

import java.util.List;
import java.util.Map;

public class OrderDetail extends BaseEntity<OrderID> {
    private OrderStatus orderStatus;
    private Money totalAmount;
    private final Map<ProductID, Product> products;

    private OrderDetail(Builder builder) {
        setId(builder.orderID);
        orderStatus = builder.orderStatus;
        totalAmount = builder.totalAmount;
        products = builder.products;
    }

    public static Builder builder() {
        return new Builder();
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public Money getTotalAmount() {
        return totalAmount;
    }

    public Map<ProductID, Product> getProducts() {
        return products;
    }

    public static final class Builder {
        private OrderID orderID;
        private OrderStatus orderStatus;
        private Money totalAmount;
        private Map<ProductID, Product> products;

        private Builder() {
        }

        public Builder orderID(OrderID val) {
            orderID = val;
            return this;
        }

        public Builder orderStatus(OrderStatus val) {
            orderStatus = val;
            return this;
        }

        public Builder totalAmount(Money val) {
            totalAmount = val;
            return this;
        }

        public Builder products(Map<ProductID, Product> val) {
            products = val;
            return this;
        }

        public OrderDetail build() {
            return new OrderDetail(this);
        }
    }
}
