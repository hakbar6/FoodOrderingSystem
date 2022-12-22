package com.food.ordering.system.payment.service.domain;

import com.food.ordering.system.payment.service.domain.dto.PaymentRequest;
import com.food.ordering.system.payment.service.domain.ports.input.message.listener.PaymentRequestMessageListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;


@Slf4j
@Validated
@Service
public class PaymentRequestMessageListenerImpl implements PaymentRequestMessageListener {
    @Override
    public void completePayment(PaymentRequest paymentRequest) {

    }

    @Override
    public void cancelPayment(PaymentRequest paymentRequest) {

    }
}
