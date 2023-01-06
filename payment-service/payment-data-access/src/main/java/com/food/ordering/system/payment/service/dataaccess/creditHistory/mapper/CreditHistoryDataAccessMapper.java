package com.food.ordering.system.payment.service.dataaccess.creditHistory.mapper;

import com.food.ordering.system.domain.valueobject.CustomerID;
import com.food.ordering.system.domain.valueobject.Money;
import com.food.ordering.system.payment.service.dataaccess.creditHistory.entity.CreditHistoryEntity;
import com.food.ordering.system.payment.service.domain.entity.paymentProcessingAggregate.CreditHistory;
import com.food.ordering.system.payment.service.domain.valueobjects.CreditHistoryId;
import org.springframework.stereotype.Component;

@Component
public class CreditHistoryDataAccessMapper {
    public CreditHistory creditHistoryEntityToCreditHistory(CreditHistoryEntity creditHistoryEntity) {
        return CreditHistory.builder()
                .creditHistoryId(new CreditHistoryId(creditHistoryEntity.getId()))
                .customerID(new CustomerID(creditHistoryEntity.getCustomerId()))
                .money(new Money(creditHistoryEntity.getAmount()))
                .transactionType(creditHistoryEntity.getType())
                .build();
    }

    public CreditHistoryEntity creditHistoryToCreditHistoryEntity(CreditHistory creditHistory) {
        return CreditHistoryEntity.builder()
                .id(creditHistory.getId().getValue())
                .customerId(creditHistory.getCustomerID().getValue())
                .amount(creditHistory.getMoney().getAmount())
                .type(creditHistory.getTransactionType())
                .build();
    }
}
