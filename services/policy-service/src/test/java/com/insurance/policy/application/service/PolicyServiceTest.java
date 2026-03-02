package com.insurance.policy.application.service;

import com.insurance.policy.domain.entity.Person;
import com.insurance.policy.domain.entity.Policy;
import com.insurance.policy.domain.entity.PolicyType;
import com.insurance.policy.domain.exception.BusinessException;
import com.insurance.policy.domain.port.out.persistence.PolicyRepositoryPort;
import com.insurance.policy.domain.port.out.traceability.TraceabilityPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PolicyServiceTest {

    @Mock
    private PolicyRepositoryPort repository;

    @Mock
    private TraceabilityPort traceability;

    @InjectMocks
    private PolicyService policyService;

    private Policy policyVida;
    private final String txId = "test-transaction-id";

    @BeforeEach
    void setUp() {
        policyVida = Policy.builder()
                .id(1L)
                .customerId(100L)
                .type(PolicyType.VIDA)
                .beneficiaries(List.of(
                        Person.builder().firstName("Bene").lastName("One").build()
                ))
                .build();

        lenient().when(traceability.traceIn(any(), anyString(), anyString())).thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));
        lenient().when(traceability.traceOut(any(), anyString(), anyString())).thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));
        lenient().when(traceability.traceError(any(Throwable.class), anyString(), anyString())).thenReturn(Mono.empty());
    }

    @Test
    void createPolicy_Vida_Success() {
        when(repository.existsByCustomerIdAndType(anyLong(), anyString())).thenReturn(Mono.just(false));
        when(repository.save(any(Policy.class))).thenReturn(Mono.just(policyVida));

        StepVerifier.create(policyService.createPolicy(policyVida, txId))
                .expectNext(policyVida)
                .verifyComplete();
    }

    @Test
    void createPolicy_Vida_AlreadyExists_Error() {
        when(repository.existsByCustomerIdAndType(anyLong(), anyString())).thenReturn(Mono.just(true));

        StepVerifier.create(policyService.createPolicy(policyVida, txId))
                .expectError(BusinessException.class)
                .verify();
    }

    @Test
    void createPolicy_Vida_TooManyBeneficiaries_Error() {
        Policy tooMany = Policy.builder()
                .type(PolicyType.VIDA)
                .customerId(100L)
                .beneficiaries(List.of(
                        new Person(), new Person(), new Person()
                ))
                .build();

        when(repository.existsByCustomerIdAndType(anyLong(), anyString())).thenReturn(Mono.just(false));

        StepVerifier.create(policyService.createPolicy(tooMany, txId))
                .expectError(BusinessException.class)
                .verify();
    }

    @Test
    void findById_Success() {
        when(repository.findById(anyLong())).thenReturn(Mono.just(policyVida));

        StepVerifier.create(policyService.findById(1L, txId))
                .expectNext(policyVida)
                .verifyComplete();
    }

    @Test
    void findById_NotFound_Error() {
        when(repository.findById(anyLong())).thenReturn(Mono.empty());

        StepVerifier.create(policyService.findById(1L, txId))
                .expectError(BusinessException.class)
                .verify();
    }

    @Test
    void findByCustomerId_Success() {
        when(repository.findByCustomerId(anyLong())).thenReturn(Flux.just(policyVida));

        StepVerifier.create(policyService.findByCustomerId(100L, txId))
                .expectNext(policyVida)
                .verifyComplete();
    }

    @Test
    void findByCustomerId_NotFound_Error() {
        when(repository.findByCustomerId(anyLong())).thenReturn(Flux.empty());

        StepVerifier.create(policyService.findByCustomerId(100L, txId))
                .expectError(BusinessException.class)
                .verify();
    }

    @Test
    void findBeneficiariesByPolicyId_Success() {
        when(repository.findById(anyLong())).thenReturn(Mono.just(policyVida));

        StepVerifier.create(policyService.findBeneficiariesByPolicyId(1L, txId))
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void findBeneficiariesByPolicyId_NotFound_Error() {
        when(repository.findById(anyLong())).thenReturn(Mono.empty());

        StepVerifier.create(policyService.findBeneficiariesByPolicyId(1L, txId))
                .expectError(BusinessException.class)
                .verify();
    }
}
