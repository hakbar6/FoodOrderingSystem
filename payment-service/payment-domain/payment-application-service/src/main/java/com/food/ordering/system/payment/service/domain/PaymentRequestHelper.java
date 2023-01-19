package com.food.ordering.system.payment.service.domain;


import com.food.ordering.system.domain.valueobject.CustomerID;
import com.food.ordering.system.payment.service.domain.dto.PaymentRequest;
import com.food.ordering.system.payment.service.domain.entity.paymentProcessingAggregate.CreditEntry;
import com.food.ordering.system.payment.service.domain.entity.paymentProcessingAggregate.CreditHistory;
import com.food.ordering.system.payment.service.domain.entity.paymentProcessingAggregate.Payment;
import com.food.ordering.system.payment.service.domain.event.PaymentEvent;
import com.food.ordering.system.payment.service.domain.exception.PaymentApplicationServiceException;
import com.food.ordering.system.payment.service.domain.mapper.PaymentDataMapper;
import com.food.ordering.system.payment.service.domain.ports.output.repository.CreditEntryRepository;
import com.food.ordering.system.payment.service.domain.ports.output.repository.CreditHistoryRepository;
import com.food.ordering.system.payment.service.domain.ports.output.repository.PaymentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/***
Class ini merupakan helper yang nanti akan kita gunakan untuk menunjang proses message listener.
 Berbeda dengan order service, dimana kita memiliki sebuah kontrak untuk application domain service nya,
 payment service tidak memerlukan hal tersebut karena service ini hanya dipanggil oleh order service saja,
 ketika ada sebuah request untuk pembayaran.

 Liat kembali desain sistem pada note sebelumnya, supaya lebih nangkep maksudnya apa....
 (note introduction, di part project overview)
***/

@Slf4j
@Component
public class PaymentRequestHelper {
    private final PaymentDomainService paymentDomainService;
    private final PaymentDataMapper paymentDataMapper;
    private final CreditEntryRepository creditEntryRepository;
    private final CreditHistoryRepository creditHistoryRepository;
    private final PaymentRepository paymentRepository;

    @Autowired
    public PaymentRequestHelper(PaymentDomainService paymentDomainService,
                                PaymentDataMapper paymentDataMapper,
                                CreditEntryRepository creditEntryRepository,
                                CreditHistoryRepository creditHistoryRepository,
                                PaymentRepository paymentRepository) {
        this.paymentDomainService = paymentDomainService;
        this.paymentDataMapper = paymentDataMapper;
        this.creditEntryRepository = creditEntryRepository;
        this.creditHistoryRepository = creditHistoryRepository;
        this.paymentRepository = paymentRepository;
    }

    @Transactional
    public PaymentEvent persistPayment(PaymentRequest paymentRequest) {
        log.info("Received payment request for order id {}",paymentRequest.getOrderId());
        Payment payment = paymentDataMapper.paymentRequestToPaymentDomain(paymentRequest);

        return domainServiceCall(payment,"initiate");
    }

    @Transactional
    public PaymentEvent persistCancelPayment(PaymentRequest paymentRequest) {
        log.info("Received payment cancel request for order id {}",paymentRequest.getOrderId());
        Optional<Payment> paymentResponse = paymentRepository
                .findByOrderId(UUID.fromString(paymentRequest.getOrderId()));
        if (paymentResponse.isEmpty()) {
            log.error("Payment for order ID {} could not be found!",paymentRequest.getOrderId());
            throw new PaymentApplicationServiceException("Could not find payment for order id " + paymentRequest.getOrderId());
        }

        Payment payment = paymentResponse.get();
        return domainServiceCall(payment,"cancel");
    }

    private PaymentEvent domainServiceCall(Payment payment, String task) {
        CreditEntry creditEntry = getCreditEntry(payment.getCustomerID());
        List<CreditHistory> creditHistories = getCreditHistories(payment.getCustomerID());
        List<String> failureMessages = new ArrayList<>();

        if (task.equals("initiate")) {
            PaymentEvent paymentEvent = paymentDomainService
                    .validateAndInitiatePayment(payment,creditEntry,creditHistories,failureMessages);
            persistDBObject(payment, creditEntry, creditHistories, failureMessages);

            return paymentEvent;
        } else if (task.equals("cancel")) {
            PaymentEvent paymentEvent = paymentDomainService
                    .validateAndCancelPayment(payment,creditEntry,creditHistories,failureMessages);
            persistDBObject(payment, creditEntry, creditHistories, failureMessages);

            return paymentEvent;
        } else {
            throw new PaymentApplicationServiceException("Wrong task!");
        }
    }

    private void persistDBObject(Payment payment,
                                 CreditEntry creditEntry,
                                 List<CreditHistory> creditHistories,
                                 List<String> failureMessages) {
        paymentRepository.save(payment);
        if (failureMessages.isEmpty()) {
            creditEntryRepository.save(creditEntry);
            creditHistoryRepository.save(creditHistories.get(creditHistories.size() - 1));
        }
    }

    private List<CreditHistory> getCreditHistories(CustomerID customerID) {
        Optional<List<CreditHistory>> creditHistories = creditHistoryRepository
                .findByCustomerId(customerID.getValue());
        if (creditHistories.isEmpty()) {
            log.error("Credit Entry for customer ID {} could not be found!", customerID.getValue());
            throw new PaymentApplicationServiceException("Could not find credit histories for customer ID " + customerID.getValue());
        }

        return creditHistories.get();
    }

    private CreditEntry getCreditEntry(CustomerID customerID) {
        Optional<CreditEntry> creditEntry = creditEntryRepository
                .findByCustomerId(customerID.getValue());
        if (creditEntry.isEmpty()) {
            log.error("Credit Entries for customer ID {} could not be found!", customerID.getValue());
            throw new PaymentApplicationServiceException("Could not find credit entry for customer ID " + customerID.getValue());
        }

        return creditEntry.get();
    }
}
