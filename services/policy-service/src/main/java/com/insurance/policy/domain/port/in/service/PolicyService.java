package com.insurance.policy.domain.port.in.service;

import com.insurance.policy.domain.entity.Person;
import com.insurance.policy.domain.entity.Policy;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PolicyService {
    Mono<Policy> createPolicy(Policy policy, String transactionId);

    Flux<Policy> findByCustomerId(Long customerId, String transactionId);

    Mono<Policy> findById(Long id, String transactionId);

    Flux<Person> findBeneficiariesByPolicyId(Long id, String transactionId);
}
