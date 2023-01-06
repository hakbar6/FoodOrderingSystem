package com.food.ordering.system.payment.service.dataaccess.creditEntry.adapter;

import com.food.ordering.system.payment.service.dataaccess.creditEntry.mapper.CreditEntryDataAccessMapper;
import com.food.ordering.system.payment.service.dataaccess.creditEntry.repository.CreditEntryJpaRepository;
import com.food.ordering.system.payment.service.domain.entity.paymentProcessingAggregate.CreditEntry;
import com.food.ordering.system.payment.service.domain.ports.output.repository.CreditEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class CreditEntryRepositoryImpl implements CreditEntryRepository {

    private final CreditEntryJpaRepository jpaRepository;
    private final CreditEntryDataAccessMapper dataAccessMapper;

    @Autowired
    public CreditEntryRepositoryImpl(CreditEntryJpaRepository jpaRepository,
                                     CreditEntryDataAccessMapper dataAccessMapper) {
        this.jpaRepository = jpaRepository;
        this.dataAccessMapper = dataAccessMapper;
    }

    @Override
    public CreditEntry save(CreditEntry creditEntry) {
        return dataAccessMapper.creditEntryEntityToCreditEntry(
                jpaRepository.save(dataAccessMapper.creditEntryToCreditEntryEntity(creditEntry))
        );
    }

    @Override
    public Optional<CreditEntry> findByCustomerId(UUID customerId) {
        return jpaRepository.findByCustomerId(customerId)
                .map(dataAccessMapper::creditEntryEntityToCreditEntry);
    }
}
