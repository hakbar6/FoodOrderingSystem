package com.food.ordering.system.payment.service.domain.entity.paymentProcessingAggregate;

import com.food.ordering.system.domain.entity.BaseEntity;
import com.food.ordering.system.domain.valueobject.CustomerID;
import com.food.ordering.system.domain.valueobject.Money;
import com.food.ordering.system.payment.service.domain.valueobjects.CreditHistoryId;
import com.food.ordering.system.payment.service.domain.valueobjects.TransactionType;

public class CreditHistory extends BaseEntity<CreditHistoryId> {
    private final CustomerID customerID;
    private final Money money;
    private final TransactionType transactionType;

    private CreditHistory(Builder builder) {
        super.setId(builder.creditHistoryId);
        customerID = builder.customerID;
        money = builder.money;
        transactionType = builder.transactionType;
    }


    public CustomerID getCustomerID() {
        return customerID;
    }

    public Money getMoney() {
        return money;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public static final class Builder {
        private CreditHistoryId creditHistoryId;
        private CustomerID customerID;
        private Money money;
        private TransactionType transactionType;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder creditHistoryId(CreditHistoryId val) {
            creditHistoryId = val;
            return this;
        }

        public Builder customerID(CustomerID val) {
            customerID = val;
            return this;
        }

        public Builder money(Money val) {
            money = val;
            return this;
        }

        public Builder transactionType(TransactionType val) {
            transactionType = val;
            return this;
        }

        public CreditHistory build() {
            return new CreditHistory(this);
        }
    }
}
