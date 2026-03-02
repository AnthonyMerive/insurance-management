package com.insurance.policy.application.service;

import com.insurance.policy.domain.entity.Person;
import com.insurance.policy.domain.entity.Policy;
import com.insurance.policy.domain.entity.PolicyType;
import com.insurance.policy.domain.exception.BusinessException;
import com.insurance.policy.domain.exception.ErrorMessage;
import com.insurance.policy.domain.port.out.persistence.PolicyRepositoryPort;
import com.insurance.policy.domain.port.out.traceability.TraceabilityPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Map;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
public class PolicyService implements com.insurance.policy.domain.port.in.service.PolicyService {

    private final PolicyRepositoryPort repository;
    private final TraceabilityPort traceability;

    private static final String CUSTOMER_ID = "customerId";
    private static final String POLICY_ID = "policyId";

    private static final String OPERATION_CREATE = "createPolicy";
    private static final String OPERATION_FIND_BY_CUSTOMER = "findByCustomerId";
    private static final String OPERATION_FIND_BY_ID = "findById";
    private static final String OPERATION_FIND_BENEFICIARIES = "findBeneficiariesByPolicyId";

    @Override
    public Mono<Policy> createPolicy(Policy policy, String transactionId) {
        return traceability.traceIn(policy, OPERATION_CREATE, transactionId)
                .flatMap(unused -> policy.getType() == PolicyType.VIDA
                        ? repository.existsByCustomerIdAndType(policy.getCustomerId(), PolicyType.VIDA.name())
                        .flatMap(exists -> policyValidations(policy, exists))
                        : repository.save(policy))
                .flatMap(saved -> traceability.traceOut(saved, OPERATION_CREATE, transactionId))
                .doOnError(throwable -> traceability.traceError(throwable, OPERATION_CREATE, transactionId));
    }

    private Mono<Policy> policyValidations(Policy policy, Boolean exists) {
        if (exists) {
            return Mono.error(new BusinessException(ErrorMessage.CUSTOMER_ALREADY_HAS_LIFE_POLICY));
        }

        if (nonNull(policy.getBeneficiaries()) && policy.getBeneficiaries().size() > 2) {
            return Mono.error(new BusinessException(ErrorMessage.LIFE_POLICY_MAX_BENEFICIARIES));
        }
        return repository.save(policy);
    }

    @Override
    public Flux<Policy> findByCustomerId(Long customerId, String transactionId) {
        return traceability.traceIn(Map.of(CUSTOMER_ID, customerId), OPERATION_FIND_BY_CUSTOMER, transactionId)
                .flatMapMany(req -> repository.findByCustomerId(customerId))
                .switchIfEmpty(Mono.error(new BusinessException(ErrorMessage.POLICY_NOT_FOUND)))
                .flatMap(policy -> traceability.traceOut(policy, OPERATION_FIND_BY_CUSTOMER, transactionId))
                .doOnError(throwable -> traceability.traceError(throwable, OPERATION_FIND_BY_CUSTOMER, transactionId));
    }

    @Override
    public Mono<Policy> findById(Long id, String transactionId) {
        return traceability.traceIn(Map.of(POLICY_ID, id), OPERATION_FIND_BY_ID, transactionId)
                .flatMap(req -> repository.findById(id))
                .switchIfEmpty(Mono.error(new BusinessException(ErrorMessage.POLICY_NOT_FOUND)))
                .flatMap(policy -> traceability.traceOut(policy, OPERATION_FIND_BY_ID, transactionId))
                .doOnError(throwable -> traceability.traceError(throwable, OPERATION_FIND_BY_ID, transactionId));
    }

    @Override
    public Flux<Person> findBeneficiariesByPolicyId(Long id, String transactionId) {
        return traceability.traceIn(Map.of(POLICY_ID, id), OPERATION_FIND_BENEFICIARIES, transactionId)
                .flatMapMany(req -> repository.findById(id))
                .switchIfEmpty(Mono.error(new BusinessException(ErrorMessage.POLICY_NOT_FOUND)))
                .flatMapIterable(policy -> isNull(policy.getBeneficiaries())
                        ? Collections.emptyList()
                        : policy.getBeneficiaries()
                )
                .flatMap(person -> traceability.traceOut(person, OPERATION_FIND_BENEFICIARIES, transactionId))
                .doOnError(throwable -> traceability.traceError(throwable, OPERATION_FIND_BENEFICIARIES, transactionId));
    }
}
