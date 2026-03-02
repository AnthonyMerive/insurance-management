package com.insurance.policy.infrastructure.adapter.out.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.insurance.policy.domain.entity.Policy;
import com.insurance.policy.domain.port.out.persistence.PolicyRepositoryPort;
import com.insurance.policy.infrastructure.adapter.out.persistence.mapper.PolicyPersistenceMapper;
import com.insurance.policy.infrastructure.adapter.out.persistence.repository.PolicyR2dbcRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class PolicyPersistenceAdapter implements PolicyRepositoryPort, PolicyPersistenceMapper {

    private final PolicyR2dbcRepository repository;
    private final ObjectMapper objectMapper;

    @Override
    public Mono<Policy> save(Policy policy) {
        return repository.save(toData(policy, objectMapper))
                .map(data -> toDomain(data, objectMapper));
    }

    @Override
    public Flux<Policy> findByCustomerId(Long customerId) {
        return repository.findByCustomerId(customerId)
                .map(data -> toDomain(data, objectMapper));
    }

    @Override
    public Mono<Policy> findById(Long id) {
        return repository.findById(id)
                .map(data -> toDomain(data, objectMapper));
    }

    @Override
    public Mono<Boolean> existsByCustomerIdAndType(Long customerId, String type) {
        return repository.existsByCustomerIdAndType(customerId, type);
    }
}