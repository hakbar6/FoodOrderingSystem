package com.food.ordering.system.domain.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Objects;

@Data
@EqualsAndHashCode
public abstract class BaseEntity<ID> {
    private ID id;
}
