package com.food.ordering.system.payment.service.domain;

import com.food.ordering.system.domain.valueobject.Money;
import com.food.ordering.system.domain.valueobject.PaymentStatus;
import com.food.ordering.system.payment.service.domain.entity.paymentProcessingAggregate.CreditEntry;
import com.food.ordering.system.payment.service.domain.entity.paymentProcessingAggregate.CreditHistory;
import com.food.ordering.system.payment.service.domain.entity.paymentProcessingAggregate.Payment;
import com.food.ordering.system.payment.service.domain.event.PaymentCancelledEvent;
import com.food.ordering.system.payment.service.domain.event.PaymentCompletedEvent;
import com.food.ordering.system.payment.service.domain.event.PaymentEvent;
import com.food.ordering.system.payment.service.domain.event.PaymentFailedEvent;
import com.food.ordering.system.payment.service.domain.valueobjects.CreditHistoryId;
import com.food.ordering.system.payment.service.domain.valueobjects.TransactionType;
import lombok.extern.slf4j.Slf4j;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
public class PaymentDomainServiceImpl implements PaymentDomainService{

    @Override
    public PaymentEvent validateAndInitiatePayment(Payment payment,
                                                   CreditEntry creditEntry,
                                                   List<CreditHistory> creditHistories,
                                                   List<String> failureMessages) {
        payment.validatePayment(failureMessages); // validate payment itself
        payment.initializePayment(); // initialize payment (start payment)
        validateCreditEntry(payment, creditEntry, failureMessages); // validate credit entry customer
        subtractCreditEntry(creditEntry, payment); // pay
        updateCreditHistory(creditHistories, payment, TransactionType.DEBIT); // update history
        validateCreditHistory(creditHistories, creditEntry, failureMessages); // check is history valid or not

        if (failureMessages.isEmpty()) {
            log.info("Payment is initiated for order id : {}",payment.getId().getValue());
            payment.updateStatus(PaymentStatus.COMPLETED);
            return new PaymentCompletedEvent(payment, ZonedDateTime.now(ZoneId.of("UTC")));
        } else {
            log.info("Payment initiation is failed for order id : {}",payment.getOrderID().getValue());
            payment.updateStatus(PaymentStatus.FAILED);
            return new PaymentFailedEvent(payment, ZonedDateTime.now(ZoneId.of("UTC")),failureMessages);
        }
    }

    @Override
    public PaymentEvent validateAndCancelPayment(Payment payment,
                                                 CreditEntry creditEntry,
                                                 List<CreditHistory> creditHistories,
                                                 List<String> failureMessages) {
        payment.validatePayment(failureMessages);
        addCreditEntry(creditEntry, payment); // tidak perlu inisiasi, karena literally proses nya adalah u/ mengembalikan uang
        updateCreditHistory(creditHistories,payment,TransactionType.CREDIT);

        if (failureMessages.isEmpty()) {
            log.info("Payment is cancelled for order id : {}",payment.getId().getValue());
            payment.updateStatus(PaymentStatus.CANCELLED);
            return new PaymentCancelledEvent(payment, ZonedDateTime.now(ZoneId.of("UTC")));
        } else {
            log.info("Payment cancellation is failed for order id : {}",payment.getOrderID().getValue());
            payment.updateStatus(PaymentStatus.FAILED);
            return new PaymentFailedEvent(payment, ZonedDateTime.now(ZoneId.of("UTC")),failureMessages);
        }
    }

    private void validateCreditEntry(Payment payment, CreditEntry creditEntry, List<String> failureMessages) {
        if (creditEntry.getTotalCreditAmount().isLessThan(payment.getPrice())) {
            log.error("Customer with ID {} doesn't have enough credit !",payment.getCustomerID().getValue());
            failureMessages.add("=== Customer with ID " + payment.getCustomerID().getValue() + " doesn't have enough credit ! ===");
        }
    }

    private void subtractCreditEntry(CreditEntry creditEntry, Payment payment) {
        creditEntry.subtractCreditAmount(payment.getPrice());
    }

    private void addCreditEntry(CreditEntry creditEntry, Payment payment) {
        creditEntry.addCreditAmount(payment.getPrice());
    }

    private void updateCreditHistory(List<CreditHistory> creditHistories, Payment payment, TransactionType transactionType) {
        creditHistories.add(CreditHistory.builder()
                        .creditHistoryId(new CreditHistoryId(UUID.randomUUID()))
                        .customerID(payment.getCustomerID())
                        .money(payment.getPrice())
                        .transactionType(transactionType)
                .build());
    }

    private void validateCreditHistory(List<CreditHistory> creditHistories, CreditEntry creditEntry, List<String> failureMessages) {
        Money totalDebit = creditHistories.stream()
                .filter(creditHistory -> creditHistory.getTransactionType() == TransactionType.DEBIT)
                .map(CreditHistory::getMoney)
                .reduce(Money.ZERO, Money::addMoney);

        Money totalCredit = creditHistories.stream()
                .filter(creditHistory -> creditHistory.getTransactionType() == TransactionType.CREDIT)
                .map(CreditHistory::getMoney)
                .reduce(Money.ZERO, Money::addMoney);

        if (totalDebit.isGreaterThan(totalCredit)) {
            log.error("Customer with ID {} doesn't have enough credit according to credit history", creditEntry.getCustomerID().getValue());
            failureMessages.add("=== Customer with ID " + creditEntry.getCustomerID().getValue() +
                    " doesn't have enough credit, according to credit history ===");
        }

        if (!creditEntry.getTotalCreditAmount().equals(totalCredit)) {
            log.error("current total credit customer with ID {} is not equal with total credit history", creditEntry.getCustomerID().getValue());
            failureMessages.add("=== current total credit customer with ID " + creditEntry.getCustomerID().getValue() +
                    "is not equal with total credit history ===");
        }
    }
}
