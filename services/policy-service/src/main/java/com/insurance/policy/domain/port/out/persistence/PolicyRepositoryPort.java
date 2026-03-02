package com.insurance.policy.domain.port.out.persistence;

import com.insurance.policy.domain.entity.Policy;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PolicyRepositoryPort {
    Mono<Policy> save(Policy policy);
    Flux<Policy> findByCustomerId(Long customerId);
    Mono<Policy> findById(Long id);
    Mono<Boolean> existsByCustomerIdAndType(Long customerId, String type);
}
