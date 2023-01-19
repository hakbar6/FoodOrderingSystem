package com.food.ordering.system.restaurant.service.dataaccess.orderApproval.adapter;

import com.food.ordering.system.restaurant.service.dataaccess.orderApproval.mapper.OrderApprovalDataAccessMapper;
import com.food.ordering.system.restaurant.service.dataaccess.orderApproval.repository.OrderApprovalJpaRepository;
import com.food.ordering.system.restaurant.service.domain.entity.OrderApproval;
import com.food.ordering.system.restaurant.service.domain.ports.output.repository.OrderApprovalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderApprovalRepositoryImpl implements OrderApprovalRepository {

    private final OrderApprovalJpaRepository jpaRepository;
    private final OrderApprovalDataAccessMapper dataAccessMapper;

    @Autowired
    public OrderApprovalRepositoryImpl(OrderApprovalJpaRepository jpaRepository,
                                       OrderApprovalDataAccessMapper dataAccessMapper) {
        this.jpaRepository = jpaRepository;
        this.dataAccessMapper = dataAccessMapper;
    }

    @Override
    public OrderApproval save(OrderApproval orderApproval) {
        return dataAccessMapper.orderApprovalEntityToOrderApprovalDomain(
                jpaRepository.save(
                        dataAccessMapper.orderApprovalDomainToOrderApprovalEntity(orderApproval)
                )
        );
    }
}
