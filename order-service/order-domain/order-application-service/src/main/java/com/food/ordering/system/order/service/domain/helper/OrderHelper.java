package com.food.ordering.system.order.service.domain.helper;

import com.food.ordering.system.order.service.domain.OrderDomainService;
import com.food.ordering.system.order.service.domain.dto.request.CreateOrderRequest;
import com.food.ordering.system.order.service.domain.entity.orderProcessingAggregate.Order;
import com.food.ordering.system.order.service.domain.entity.restaurantAggregate.Restaurant;
import com.food.ordering.system.order.service.domain.event.OrderCreatedEvent;
import com.food.ordering.system.order.service.domain.execption.OrderDomainException;
import com.food.ordering.system.order.service.domain.mapper.OrderDataMapper;
import com.food.ordering.system.order.service.domain.ports.output.repository.CustomerRepository;
import com.food.ordering.system.order.service.domain.ports.output.repository.OrderRepository;
import com.food.ordering.system.order.service.domain.ports.output.repository.RestaurantRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Component
public class OrderHelper {
    private final OrderDomainService orderDomainService;
    private final OrderRepository orderRepository;
    private final RestaurantRepository restaurantRepository;
    private final CustomerRepository customerRepository;
    private final OrderDataMapper mapper;


    @Autowired
    public OrderHelper(OrderDomainService orderDomainService,
                       OrderRepository orderRepository,
                       RestaurantRepository restaurantRepository,
                       CustomerRepository customerRepository,
                       OrderDataMapper mapper) {
        this.orderDomainService = orderDomainService;
        this.orderRepository = orderRepository;
        this.restaurantRepository = restaurantRepository;
        this.customerRepository = customerRepository;
        this.mapper = mapper;
    }

    @Transactional
    public OrderCreatedEvent persistOrder(CreateOrderRequest request) {
        checkCustomer(request);
        Restaurant restaurant = checkAndSetRestaurantInformation(request);
        Order order = mapper.createOrderRequestToOrderDomainEntity(request);
        OrderCreatedEvent orderCreatedEvent = orderDomainService.initializeAndValidateOrder(order,restaurant);
        saveOrder(order);
        log.info("Order with ID {} is created and saved!",order.getId().getValue());
        return orderCreatedEvent;
    }

    private void checkCustomer(CreateOrderRequest request) {
        if (customerRepository.findCustomerById(request.getCustomerId()).isEmpty()) {
            log.warn("Customer with the {} does not exist!", request.getCustomerId());
            throw new OrderDomainException("Customer with the " +request.getCustomerId()+
                    " does not exist!");
        }
    }

    private Restaurant checkAndSetRestaurantInformation(CreateOrderRequest request) {
        Restaurant restaurant = mapper.createOrderRequestToRestaurant(request);
        Optional<Restaurant> result = restaurantRepository.findRestaurantInformation(restaurant);
        if (result.isEmpty()) {
            throw new OrderDomainException("Could not find restaurant with restaurant ID : " +
                    request.getRestaurantId());
        }
        return result.get();
    }

    private void saveOrder(Order order) {
        Order output = orderRepository.saveOrder(order);
        if (output == null) {
            throw new OrderDomainException("Could not save order!");
        }
        log.info("Order with ID {} saved!",output.getId()) ;
    }
}
