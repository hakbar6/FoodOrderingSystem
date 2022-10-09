package com.food.ordering.system.order.service.domain.entity.orderProcessingAggregate;

import com.food.ordering.system.domain.entity.BaseEntity;
import com.food.ordering.system.domain.valueobject.Money;
import com.food.ordering.system.domain.valueobject.OrderID;
import com.food.ordering.system.order.service.domain.valueobjects.OrderItemId;

public class OrderItem extends BaseEntity<OrderItemId> {
    private OrderID orderID;

    private final Product product; // property ini digunakan untuk mengecek apakah harga yang diinput user benar atau tidak
    private final Integer quantity;
    private final Money price; // harga yang diinput user
    private final Money subTotal; // dikalkulasi otomatis = price * quantity

    void initializeOrderItem(OrderID orderID, OrderItemId orderItemId) {
        super.setId(orderItemId);
        this.orderID = orderID;
    }

    boolean isPriceValid() { // mengecek apakah input harga sudah benar
        return price.isGreaterThanZero() && // harga diinput merupakan angka yang tidak nol
                price.equals(product.getPrice()) && // harga yang tercantum dengan harga produk asli benar
                subTotal.equals(price.multiply(quantity)); // apakah harga sub total benar atau tidak
    }

    private OrderItem(Builder builder) {
        super.setId(builder.orderItemId);
        product = builder.product;
        quantity = builder.quantity;
        price = builder.price;
        subTotal = builder.subTotal;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Product getProduct() {
        return product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Money getPrice() {
        return price;
    }

    public Money getSubTotal() {
        return subTotal;
    }


    public static final class Builder {
        private OrderItemId orderItemId;
        private Product product;
        private Integer quantity;
        private Money price;
        private Money subTotal;

        private Builder() {
        }

        public Builder id(OrderItemId val) {
            orderItemId = val;
            return this;
        }

        public Builder product(Product val) {
            product = val;
            return this;
        }

        public Builder quantity(Integer val) {
            quantity = val;
            return this;
        }

        public Builder price(Money val) {
            price = val;
            return this;
        }

        public Builder subTotal(Money val) {
            subTotal = val;
            return this;
        }

        public OrderItem build() {
            return new OrderItem(this);
        }
    }
}
