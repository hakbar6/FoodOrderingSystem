package com.food.ordering.system.payment.service.dataaccess.creditEntry.mapper;

import com.food.ordering.system.domain.valueobject.CustomerID;
import com.food.ordering.system.domain.valueobject.Money;
import com.food.ordering.system.payment.service.dataaccess.creditEntry.entity.CreditEntryEntity;
import com.food.ordering.system.payment.service.domain.entity.paymentProcessingAggregate.CreditEntry;
import com.food.ordering.system.payment.service.domain.valueobjects.CreditEntryId;
import org.springframework.stereotype.Component;

@Component
public class CreditEntryDataAccessMapper {
    public CreditEntry creditEntryEntityToCreditEntry(CreditEntryEntity entity) {
        return CreditEntry.builder()
                .creditEntryId(new CreditEntryId(entity.getId()))
                .customerID(new CustomerID(entity.getCustomerId()))
                .totalCreditAmount(new Money(entity.getTotalCreditAmount()))
                .build();
    }

    public CreditEntryEntity creditEntryToCreditEntryEntity(CreditEntry creditEntry) {
        return CreditEntryEntity.builder()
                .id(creditEntry.getId().getValue())
                .customerId(creditEntry.getCustomerID().getValue())
                .totalCreditAmount(creditEntry.getTotalCreditAmount().getAmount())
                .build();
    }
}
