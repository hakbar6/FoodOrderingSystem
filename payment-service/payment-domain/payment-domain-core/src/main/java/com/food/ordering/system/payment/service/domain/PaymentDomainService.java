package com.food.ordering.system.payment.service.domain;

import com.food.ordering.system.payment.service.domain.entity.paymentProcessingAggregate.CreditEntry;
import com.food.ordering.system.payment.service.domain.entity.paymentProcessingAggregate.CreditHistory;
import com.food.ordering.system.payment.service.domain.entity.paymentProcessingAggregate.Payment;
import com.food.ordering.system.payment.service.domain.event.PaymentEvent;

import java.util.List;

public interface PaymentDomainService {
    PaymentEvent validateAndInitiatePayment(Payment payment,
                                            CreditEntry creditEntry,
                                            List<CreditHistory> creditHistories,
                                            List<String> failureMessages);

    PaymentEvent validateAndCancelPayment(Payment payment,
                                          CreditEntry creditEntry,
                                          List<CreditHistory> creditHistories,
                                          List<String> failureMessages);
}
