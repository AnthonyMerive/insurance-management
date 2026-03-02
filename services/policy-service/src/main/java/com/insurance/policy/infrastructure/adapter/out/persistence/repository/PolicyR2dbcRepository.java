package com.insurance.policy.infrastructure.adapter.out.persistence.repository;

import com.insurance.policy.infrastructure.adapter.out.persistence.data.PolicyEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PolicyR2dbcRepository extends ReactiveCrudRepository<PolicyEntity, Long> {
    Flux<PolicyEntity> findByCustomerId(Long customerId);
    Mono<Boolean> existsByCustomerIdAndType(Long customerId, String type);

    @Query("CALL GET_POLICY_DETAILS(:id)")
    Mono<PolicyEntity> getPolicyDetailsStoredProc(Long id);
}
