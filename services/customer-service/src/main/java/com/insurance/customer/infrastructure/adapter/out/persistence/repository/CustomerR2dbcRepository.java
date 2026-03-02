package com.insurance.customer.infrastructure.adapter.out.persistence.repository;

import com.insurance.customer.infrastructure.adapter.out.persistence.data.CustomerEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface CustomerR2dbcRepository extends ReactiveCrudRepository<CustomerEntity, Long> {
}
