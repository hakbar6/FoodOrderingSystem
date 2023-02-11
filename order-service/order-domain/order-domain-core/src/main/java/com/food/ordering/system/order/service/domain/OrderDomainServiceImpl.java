package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.order.service.domain.entity.orderProcessingAggregate.Order;
import com.food.ordering.system.order.service.domain.entity.orderProcessingAggregate.Product;
import com.food.ordering.system.order.service.domain.entity.restaurantAggregate.Restaurant;
import com.food.ordering.system.order.service.domain.event.OrderCancelledEvent;
import com.food.ordering.system.order.service.domain.event.OrderCreatedEvent;
import com.food.ordering.system.order.service.domain.event.OrderPaidEvent;
import com.food.ordering.system.order.service.domain.execption.OrderDomainException;
import lombok.extern.slf4j.Slf4j;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Slf4j
class OrderDomainServiceImpl implements OrderDomainService{

    private final String UTC = "UTC";

    @Override
    public OrderCreatedEvent initializeAndValidateOrder(Order order, Restaurant restaurant) {
        // inisialisasi dan valdiasi order
        validateRestaurant(restaurant); // validasi restaurant
        setOrderProductInformation(order, restaurant);
        order.validateOrder();
        order.initializeOrder();
        log.info("Order with ID : {} is initiated!",order.getId().getValue());
        return new OrderCreatedEvent(order, ZonedDateTime.now(ZoneId.of(UTC)));
    }

    private void validateRestaurant(Restaurant restaurant) {
        if (!restaurant.isActive()) {
            throw new OrderDomainException("The restaurant with id "+
                    restaurant.getId().getValue() +" is currently not active!");
        }
    }

    private void setOrderProductInformation(Order order, Restaurant restaurant) {
        // harga dan nama produk kita ambil dari db
        // sehingga, pada object order yang nanti akan diinput oleh user, hanya id yang dicantumkan
        order.getItems().forEach(orderItem -> {
            Product currentProduct = orderItem.getProduct();
            Product restaurantProduct = restaurant.getProducts().get(currentProduct.getId());
            if (restaurantProduct != null){
                currentProduct.updateWithConfirmedNameAndPrice(restaurantProduct.getName(),restaurantProduct.getPrice());
            } else {
                throw new OrderDomainException("Your order item is not correct!");
            }
        });
    }

    @Override
    public OrderPaidEvent payOrder(Order order) {
        order.pay();
        log.info("Order with ID : {} is paid!",order.getId().getValue());
        return new OrderPaidEvent(order,ZonedDateTime.now(ZoneId.of(UTC)));
    }

    @Override
    public OrderCancelledEvent cancellingOrderPayment(Order order, List<String> failureMessages) {
        // digunakan untuk inisialisasi cancel order JIKA order ditolak oleh restaurant
        // method ini akan dipanggil untuk menerbitkan event order cancelled event yang kemudian akan
        // memicu payment service untuk membatalkan pembayaran yang telah dilakukan
        order.initCancel(failureMessages);
        log.info("Cancelling order with ID  : {}",order.getId().getValue());
        return new OrderCancelledEvent(order,ZonedDateTime.now(ZoneId.of(UTC)));
    }

    @Override
    public void approveOrder(Order order) {
        // digunakan untuk approve order
        order.approve();
        log.info("Order with ID : {} is approved!",order.getId().getValue());
    }

    @Override
    public void cancelOrder(Order order, List<String> failureMessages) {
        // digunakan untuk cancel order jika SUDAH DITOLAK oleh payment service
        order.cancel(failureMessages);
        log.info("Order with ID : {} is cancelled!",order.getId().getValue());
    }


}
