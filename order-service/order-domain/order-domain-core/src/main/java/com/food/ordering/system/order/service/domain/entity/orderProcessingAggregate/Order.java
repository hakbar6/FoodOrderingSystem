package com.food.ordering.system.order.service.domain.entity.orderProcessingAggregate;

import com.food.ordering.system.domain.entity.AggregateRoot;
import com.food.ordering.system.domain.valueobject.*;
import com.food.ordering.system.order.service.domain.execption.OrderDomainException;
import com.food.ordering.system.order.service.domain.valueobjects.OrderItemId;
import com.food.ordering.system.order.service.domain.valueobjects.StreetAddress;
import com.food.ordering.system.order.service.domain.valueobjects.TrackingId;

import java.util.List;
import java.util.UUID;

public class Order extends AggregateRoot<OrderID> {
    private final CustomerId customerId;
    private final RestaurantID restaurantID;
    private final StreetAddress deliveryAddress;
    private final Money price;
    private final List<OrderItem> items;

    private TrackingId trackingId;
    private OrderStatus orderStatus;
    private List<String> failureMessage;

    public void initializeOrder() { // fungsi untuk inisialisasi order
        super.setId(new OrderID(UUID.randomUUID()));
        this.trackingId = new TrackingId(UUID.randomUUID());
        this.orderStatus = OrderStatus.PENDING;
        initializeOrderItems();
    }

    public void validateOrder() { // fungsi untuk validasi order
        validateInitializeOrder();
        validateTotalPrice();
        validateItemsPrice();
    }

    private void validateItemsPrice() { // validate jumlah harga yang tercantum dengan jumlah pada order item
        Money subTotalItem = items.stream().map(orderItem -> {
            validateItemPrice(orderItem);
            return orderItem.getSubTotal();
        }).reduce(Money.ZERO, Money::addMoney); // fungsi penjumlahan seluruh money yang ada di list item

        if (!subTotalItem.equals(this.price)){
            throw new OrderDomainException(
                    "Total Price " + this.price.getAmount() + " is not equal to order items total : " +
                            subTotalItem.getAmount()
            );
        }
    }

    private void validateItemPrice(OrderItem item) {
        if (!item.isPriceValid()) {
            throw new OrderDomainException("Item Order Price is not valid!");
        }
    }

    private void validateTotalPrice() {
        if (this.price == null || !this.price.isGreaterThanZero()) {
            throw new OrderDomainException("Total price must greater than zero");
        }
    }

    // digunakan untuk mengecek apakah sebelum melakukan inisialisasi order sudah tepat
    private void validateInitializeOrder() {
        if (getId() != null || orderStatus != null){ // sebelum dilakukan inisialisasi, id dan status harus null
            throw new OrderDomainException("Order is not in correct state for initialization!");
        }
    }

    private void initializeOrderItems() {
        long itemId = 1;
        for (OrderItem orderItem: items) {
            orderItem.initializeOrderItem(getId(), new OrderItemId(itemId++));
        }
    }

    public void pay() { // ketika payment sukses
        if(orderStatus != OrderStatus.PENDING) {
            throw new OrderDomainException("Order is not in correct state for pay operation!");
        }
        orderStatus = OrderStatus.PAID;
    }

    public void approve() { // ketika pesan ke restaurant sukses setelah membayar
        if(orderStatus != OrderStatus.PAID) {
            throw new OrderDomainException("Order is not in correct state for approve operation!");
        }
        orderStatus = OrderStatus.APPROVED;
    }

    public void initCancel(List<String> failureMessage) {
        if(orderStatus != OrderStatus.PAID) {
            throw new OrderDomainException("Order is not in correct state for init cancel operation!");
        }
        addFailureMessage(failureMessage);
        orderStatus = OrderStatus.CANCELLING;
    }

    private void addFailureMessage(List<String> failureMessage) {
        if (this.failureMessage != null){
            this.failureMessage.addAll(
                    failureMessage.stream().filter(
                            message -> !message.isEmpty()
                    ).toList()
            );
        }
        if (this.failureMessage == null){
            this.failureMessage = failureMessage;
        }
    }

    public void cancel() {
        if (orderStatus != OrderStatus.PENDING && orderStatus != OrderStatus.PAID) {
            throw new OrderDomainException("Order is not in correct state for cancel operation!");
        }
        orderStatus = OrderStatus.CANCELLED;
    }

    private Order(Builder builder) {
        super.setId(builder.orderID);
        customerId = builder.customerId;
        restaurantID = builder.restaurantID;
        deliveryAddress = builder.deliveryAddress;
        price = builder.price;
        items = builder.items;
        trackingId = builder.trackingId;
        orderStatus = builder.orderStatus;
        failureMessage = builder.failureMessage;
    }

    public static Builder newBuilder() {
        return new Builder();
    }


    public CustomerId getCustomerId() {
        return customerId;
    }

    public RestaurantID getRestaurantID() {
        return restaurantID;
    }

    public StreetAddress getDeliveryAddress() {
        return deliveryAddress;
    }

    public Money getPrice() {
        return price;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public TrackingId getTrackingId() {
        return trackingId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public List<String> getFailureMessage() {
        return failureMessage;
    }

    public static final class Builder {
        private OrderID orderID;
        private CustomerId customerId;
        private RestaurantID restaurantID;
        private StreetAddress deliveryAddress;
        private Money price;
        private List<OrderItem> items;
        private TrackingId trackingId;
        private OrderStatus orderStatus;
        private List<String> failureMessage;

        private Builder() {
        }

        public Builder id(OrderID val) {
            orderID = val;
            return this;
        }

        public Builder customerId(CustomerId val) {
            customerId = val;
            return this;
        }

        public Builder restaurantID(RestaurantID val) {
            restaurantID = val;
            return this;
        }

        public Builder deliveryAddress(StreetAddress val) {
            deliveryAddress = val;
            return this;
        }

        public Builder price(Money val) {
            price = val;
            return this;
        }

        public Builder items(List<OrderItem> val) {
            items = val;
            return this;
        }

        public Builder trackingId(TrackingId val) {
            trackingId = val;
            return this;
        }

        public Builder orderStatus(OrderStatus val) {
            orderStatus = val;
            return this;
        }

        public Builder failureMessage(List<String> val) {
            failureMessage = val;
            return this;
        }

        public Order build() {
            return new Order(this);
        }
    }
}
