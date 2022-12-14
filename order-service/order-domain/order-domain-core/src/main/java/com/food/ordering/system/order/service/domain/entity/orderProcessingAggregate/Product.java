package com.food.ordering.system.order.service.domain.entity.orderProcessingAggregate;

import com.food.ordering.system.domain.entity.BaseEntity;
import com.food.ordering.system.domain.valueobject.Money;
import com.food.ordering.system.domain.valueobject.ProductID;

public class Product extends BaseEntity<ProductID> {
    private String name;
    private Money price;

    public Product(ProductID productID, String name, Money price) {
        super.setId(productID);
        this.name = name;
        this.price = price;
    }

    public Product(ProductID productId) {
        super.setId(productId);
    }

    public String getName() {
        return name;
    }

    public Money getPrice() {
        return price;
    }

    public void updateWithConfirmedNameAndPrice(String name, Money price) {
        this.name = name;
        this.price = price;
    }
}
