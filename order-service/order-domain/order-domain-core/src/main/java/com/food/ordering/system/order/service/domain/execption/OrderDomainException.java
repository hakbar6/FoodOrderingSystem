package com.food.ordering.system.order.service.domain.execption;

import com.food.ordering.system.domain.exception.DomainException;
/*
Exception ini digunakan untuk error-error yang berkaitan dengan proses business logic pada domain.
Semisal, untuk proses validate and initialize order, ternyata ditemukan error pada step check total price,
maka exception yang akan dikembalikan adalah order domain exception.
*/

public class OrderDomainException extends DomainException {
    public OrderDomainException(String message) {
        super(message);
    }

    public OrderDomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
