package com.food.ordering.system.payment.service.domain.entity.paymentProcessingAggregate;

import com.food.ordering.system.domain.entity.BaseEntity;
import com.food.ordering.system.domain.valueobject.CustomerID;
import com.food.ordering.system.domain.valueobject.Money;
import com.food.ordering.system.payment.service.domain.valueobjects.CreditEntryId;

public class CreditEntry extends BaseEntity<CreditEntryId> {
    private final CustomerID customerID;
    private Money totalCreditAmount;

    public void addCreditAmount(Money creditAmount) {
        totalCreditAmount = totalCreditAmount.add(creditAmount);
    }

    public void subtractCreditAmount(Money creditAmount) {
        totalCreditAmount = totalCreditAmount.subtract(creditAmount);
    }

    private CreditEntry(Builder builder) {
        super.setId(builder.creditEntryId);
        customerID = builder.customerID;
        totalCreditAmount = builder.totalCreditAmount;
    }


    public CustomerID getCustomerID() {
        return customerID;
    }

    public Money getTotalCreditAmount() {
        return totalCreditAmount;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private CreditEntryId creditEntryId;
        private CustomerID customerID;
        private Money totalCreditAmount;

        private Builder() {
        }

        public Builder creditEntryId(CreditEntryId val) {
            creditEntryId = val;
            return this;
        }

        public Builder customerID(CustomerID val) {
            customerID = val;
            return this;
        }

        public Builder totalCreditAmount(Money val) {
            totalCreditAmount = val;
            return this;
        }

        public CreditEntry build() {
            return new CreditEntry(this);
        }
    }
}
