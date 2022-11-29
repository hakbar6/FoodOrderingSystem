package com.food.ordering.system.order.service.dataaccess.customer.mapper;


import com.food.ordering.system.domain.valueobject.CustomerID;
import com.food.ordering.system.order.service.dataaccess.customer.entity.CustomerEntity;
import com.food.ordering.system.order.service.domain.entity.customerAggregate.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerDataAccessMapper {

    public Customer customerEntityToCustomerDomain(CustomerEntity customerEntity) {
        return new Customer(new CustomerID(customerEntity.getId()));
    }
}
