package com.food.ordering.system.order.service.domain.entity.customerAggregate;

import com.food.ordering.system.domain.entity.AggregateRoot;
import com.food.ordering.system.domain.valueobject.CustomerID;

// sementara, leave this class empty karena digunakan hanya untuk mengecek keberadaan customer
public class Customer extends AggregateRoot<CustomerID> {
    public Customer() {
    }

    public Customer(CustomerID id) {
        super.setId(id);
    }
}
