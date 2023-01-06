package com.food.ordering.system.payment.service.dataaccess.creditHistory.adapter;

import com.food.ordering.system.payment.service.dataaccess.creditHistory.mapper.CreditHistoryDataAccessMapper;
import com.food.ordering.system.payment.service.dataaccess.creditHistory.repository.CreditHistoryJpaRepository;
import com.food.ordering.system.payment.service.domain.entity.paymentProcessingAggregate.CreditHistory;
import com.food.ordering.system.payment.service.domain.ports.output.repository.CreditHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class CreditHistoryRepositoryImpl implements CreditHistoryRepository {

    private final CreditHistoryJpaRepository jpaRepository;
    private final CreditHistoryDataAccessMapper dataAccessMapper;

    @Autowired
    public CreditHistoryRepositoryImpl(CreditHistoryJpaRepository jpaRepository,
                                       CreditHistoryDataAccessMapper dataAccessMapper) {
        this.jpaRepository = jpaRepository;
        this.dataAccessMapper = dataAccessMapper;
    }

    @Override
    public CreditHistory save(CreditHistory creditHistory) {
        return dataAccessMapper.creditHistoryEntityToCreditHistory(
               jpaRepository.save(dataAccessMapper.creditHistoryToCreditHistoryEntity(creditHistory))
        );
    }

    @Override
    public Optional<List<CreditHistory>> findByCustomerId(UUID customerId) {
        return jpaRepository.findByCustomerId(customerId)
                .map(creditHistoryEntities -> creditHistoryEntities.stream().map(
                        dataAccessMapper::creditHistoryEntityToCreditHistory
                ).collect(Collectors.toList()));
    }
}
