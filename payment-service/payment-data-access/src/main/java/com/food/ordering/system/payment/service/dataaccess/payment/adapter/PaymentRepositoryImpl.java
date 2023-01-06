package com.food.ordering.system.payment.service.dataaccess.payment.adapter;

import com.food.ordering.system.payment.service.dataaccess.payment.mapper.PaymentDataAccessMapper;
import com.food.ordering.system.payment.service.dataaccess.payment.repository.PaymentJpaRepository;
import com.food.ordering.system.payment.service.domain.entity.paymentProcessingAggregate.Payment;
import com.food.ordering.system.payment.service.domain.ports.output.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class PaymentRepositoryImpl implements PaymentRepository {

    private final PaymentJpaRepository paymentJpaRepository;
    private final PaymentDataAccessMapper dataAccessMapper;

    @Autowired
    public PaymentRepositoryImpl(PaymentJpaRepository paymentJpaRepository,
                                 PaymentDataAccessMapper dataAccessMapper) {
        this.paymentJpaRepository = paymentJpaRepository;
        this.dataAccessMapper = dataAccessMapper;
    }

    @Override
    public Payment save(Payment payment) {
        return dataAccessMapper.paymentEntityToPayment(
                paymentJpaRepository.save(dataAccessMapper.paymentToPaymentEntity(payment))
        );
    }

    @Override
    public Optional<Payment> findByOrderId(UUID orderId) {
        return paymentJpaRepository.findByOrderId(orderId)
                .map(dataAccessMapper::paymentEntityToPayment);
    }
}
