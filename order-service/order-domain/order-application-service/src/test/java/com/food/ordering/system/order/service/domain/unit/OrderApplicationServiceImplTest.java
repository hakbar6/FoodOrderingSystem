package com.food.ordering.system.order.service.domain.unit;


import com.food.ordering.system.domain.valueobject.*;
import com.food.ordering.system.order.service.domain.OrderDomainService;
import com.food.ordering.system.order.service.domain.OrderTestConfiguration;
import com.food.ordering.system.order.service.domain.dto.request.CreateOrderRequest;
import com.food.ordering.system.order.service.domain.dto.response.CreateOrderResponse;
import com.food.ordering.system.order.service.domain.dto.request.OrderAddress;
import com.food.ordering.system.order.service.domain.dto.request.OrderItemRequest;
import com.food.ordering.system.order.service.domain.entity.customerAggregate.Customer;
import com.food.ordering.system.order.service.domain.entity.orderProcessingAggregate.Order;
import com.food.ordering.system.order.service.domain.entity.orderProcessingAggregate.Product;
import com.food.ordering.system.order.service.domain.entity.restaurantAggregate.Restaurant;
import com.food.ordering.system.order.service.domain.execption.OrderDomainException;
import com.food.ordering.system.order.service.domain.mapper.OrderDataMapper;
import com.food.ordering.system.order.service.domain.ports.input.service.OrderApplicationService;
import com.food.ordering.system.order.service.domain.ports.output.repository.CustomerRepository;
import com.food.ordering.system.order.service.domain.ports.output.repository.OrderRepository;
import com.food.ordering.system.order.service.domain.ports.output.repository.RestaurantRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = OrderTestConfiguration.class)
public class OrderApplicationServiceImplTest {
    @Autowired
    private OrderApplicationService orderApplicationService;

    @Autowired
    private OrderDataMapper orderDataMapper;

    @Autowired
    private OrderDomainService orderDomainService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    private CreateOrderRequest createOrderRequest; // case 1 (sukses)
    private CreateOrderRequest createOrderRequestWrongPrice; // case 2 (input price salah)
    private CreateOrderRequest createOrderRequestWrongProductPrice; // case 3 (harga produk salah)
    private final UUID CUSTOMER_ID = UUID.fromString("52e5b4b1-9953-463e-a6df-c69782e621e4");
    private final UUID RESTAURANT_ID = UUID.fromString("3e9b07db-c537-461b-a811-1d1787346d77");
    private final UUID PRODUCT_ID_1 = UUID.fromString("453f6c26-5ece-494c-97f6-33da2e312939");
    private final UUID PRODUCT_ID_2 = UUID.fromString("6dd81cb3-7323-4c30-a15b-8d2a806ff4d0");
    private final UUID ORDER_ID = UUID.fromString("9356a42e-f2f8-4d99-aa78-4eb4d2506577");
    private final Map<ProductID,Product> products = new HashMap<>();
    private final BigDecimal PRICE = new BigDecimal("200.00");
    private Restaurant restaurantResponse;

    @BeforeAll
    public void init() {
        createOrderRequest = CreateOrderRequest.builder()
                .customerId(CUSTOMER_ID)
                .restaurantId(RESTAURANT_ID)
                .address(OrderAddress.builder()
                        .street("street_1")
                        .postalCode("1000AB")
                        .city("Paris")
                        .build())
                .price(PRICE)
                .items(List.of(
                        OrderItemRequest.builder()
                                .productId(PRODUCT_ID_1)
                                .price(new BigDecimal("50.00"))
                                .quantity(1)
                                .subTotal(new BigDecimal("50.00"))
                                .build(),
                        OrderItemRequest.builder()
                                .productId(PRODUCT_ID_2)
                                .price(new BigDecimal("50.00"))
                                .quantity(3)
                                .subTotal(new BigDecimal("150.00"))
                                .build()
                ))
                .build();

        createOrderRequestWrongPrice = CreateOrderRequest.builder()
                .customerId(CUSTOMER_ID)
                .restaurantId(RESTAURANT_ID)
                .address(OrderAddress.builder()
                        .street("street_1")
                        .postalCode("1000AB")
                        .city("Paris")
                        .build())
                .price(new BigDecimal("300.00")) // wrong input pricec
                .items(List.of(
                        OrderItemRequest.builder()
                                .productId(PRODUCT_ID_1)
                                .price(new BigDecimal("50.00"))
                                .quantity(1)
                                .subTotal(new BigDecimal("50.00"))
                                .build(),
                        OrderItemRequest.builder()
                                .productId(PRODUCT_ID_2)
                                .price(new BigDecimal("50.00"))
                                .quantity(3)
                                .subTotal(new BigDecimal("150.00"))
                                .build()
                ))
                .build();

        createOrderRequestWrongProductPrice = CreateOrderRequest.builder()
                .customerId(CUSTOMER_ID)
                .restaurantId(RESTAURANT_ID)
                .address(OrderAddress.builder()
                        .street("street_1")
                        .postalCode("1000AB")
                        .city("Paris")
                        .build())
                .price(new BigDecimal("210.00"))
                .items(List.of(
                        OrderItemRequest.builder()
                                .productId(PRODUCT_ID_1)
                                .price(new BigDecimal("60.00")) // wrong product price
                                .quantity(1)
                                .subTotal(new BigDecimal("60.00")) // wrong product price
                                .build(),
                        OrderItemRequest.builder()
                                .productId(PRODUCT_ID_2)
                                .price(new BigDecimal("50.00"))
                                .quantity(3)
                                .subTotal(new BigDecimal("150.00"))
                                .build()
                ))
                .build();
        Customer customer = new Customer();
        customer.setId(new CustomerID(CUSTOMER_ID));

        Product product_1 = new Product(new ProductID(PRODUCT_ID_1),"product-1",new Money(new BigDecimal("50.00")));
        Product product_2 = new Product(new ProductID(PRODUCT_ID_2),"product-2",new Money(new BigDecimal("50.00")));

        products.put(product_1.getId(),product_1);
        products.put(product_2.getId(),product_2);

        restaurantResponse = Restaurant.builder() // mock, seolah-olah ambil dari db
                .id(new RestaurantID(RESTAURANT_ID))
                .active(true)
                .products(products)
                .build();

        Order order = orderDataMapper.createOrderRequestToOrderDomainEntity(createOrderRequest);
        order.setId(new OrderID(ORDER_ID));

        Mockito.when(customerRepository.findCustomerById(CUSTOMER_ID)).thenReturn(Optional.of(customer));
        Mockito.when(orderRepository.saveOrder(any(Order.class))).thenReturn(order);
    }

    @Test
    public void givenValidCreateOrderRequest_withValidRestaurantAndValidProduct_thenReturnSuccess() {
        Mockito.when(restaurantRepository.findRestaurantInformation(
                orderDataMapper.createOrderRequestToRestaurant(createOrderRequest)
        )).thenReturn(Optional.of(restaurantResponse));

        CreateOrderResponse result = orderApplicationService.createOrder(createOrderRequest);
        Assertions.assertEquals(result.getOrderStatus(),OrderStatus.PENDING);
        Assertions.assertEquals(result.getMessage(),"SUCCESS");
        Assertions.assertNotNull(result.getTrackingId());
    }

    @Test
    public void givenValidCreateOrderRequest_withNotValidPriceInput_thenReturnError() {
        Mockito.when(restaurantRepository.findRestaurantInformation(
                orderDataMapper.createOrderRequestToRestaurant(createOrderRequest)
        )).thenReturn(Optional.of(restaurantResponse));

        OrderDomainException result = Assertions.assertThrows(OrderDomainException.class,
                () -> orderApplicationService.createOrder(createOrderRequestWrongPrice));
        Assertions.assertEquals("Total Price 300.00 is not equal to order items total : 200.00",result.getMessage());
    }

    @Test
    public void givenValidCreateOrderRequest_withNotValidItemPriceInput_thenReturnError() {
        Mockito.when(restaurantRepository.findRestaurantInformation(
                orderDataMapper.createOrderRequestToRestaurant(createOrderRequest)
        )).thenReturn(Optional.of(restaurantResponse));

        OrderDomainException result = Assertions.assertThrows(OrderDomainException.class,
                () -> orderApplicationService.createOrder(createOrderRequestWrongProductPrice));
        Assertions.assertEquals("Item Order Price is not valid!",result.getMessage());
    }

    @Test
    public void givenValidCreateOrderRequest_withPassiveRestaurant_thenReturnError() {
        Restaurant restaurantResponse2 = Restaurant.builder() // mock, seolah-olah ambil dari db
                .id(new RestaurantID(RESTAURANT_ID))
                .active(false)
                .products(products)
                .build();

        Mockito.when(restaurantRepository.findRestaurantInformation(
                orderDataMapper.createOrderRequestToRestaurant(createOrderRequest)
        )).thenReturn(Optional.of(restaurantResponse2));

        OrderDomainException result = Assertions.assertThrows(OrderDomainException.class,
                () -> orderApplicationService.createOrder(createOrderRequest));
        Assertions.assertEquals("The restaurant with id 3e9b07db-c537-461b-a811-1d1787346d77 is currently not active!",result.getMessage());
    }
}
