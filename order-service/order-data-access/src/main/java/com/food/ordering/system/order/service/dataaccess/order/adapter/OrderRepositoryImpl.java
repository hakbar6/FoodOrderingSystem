package com.food.ordering.system.order.service.dataaccess.order.adapter;

import com.food.ordering.system.domain.valueobject.OrderID;
import com.food.ordering.system.order.service.dataaccess.order.mapper.OrderDataAccessMapper;
import com.food.ordering.system.order.service.dataaccess.order.repository.OrderJpaRepository;
import com.food.ordering.system.order.service.domain.entity.orderProcessingAggregate.Order;
import com.food.ordering.system.order.service.domain.ports.output.repository.OrderRepository;
import com.food.ordering.system.order.service.domain.valueobjects.TrackingId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderJpaRepository orderJpaRepository;
    private final OrderDataAccessMapper mapper;

    @Autowired // optional
    public OrderRepositoryImpl(OrderJpaRepository orderJpaRepository, OrderDataAccessMapper mapper) {
        this.orderJpaRepository = orderJpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Order saveOrder(Order order) {
        return mapper.orderEntityToOrderDomain(
                orderJpaRepository.save(mapper.orderDomainToOrderEntity(order))
        );
    }

    @Override
    public Optional<Order> findOrderByTrackingId(TrackingId trackingId) {
        return orderJpaRepository.findOrderEntityByTrackingId(trackingId.getValue())
                .map(mapper::orderEntityToOrderDomain);
    }

    @Override
    public Optional<Order> findByOrderId(OrderID orderID) {
        return orderJpaRepository.findOrderEntityById(orderID.getValue())
                .map(mapper::orderEntityToOrderDomain);
    }
}
