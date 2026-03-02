package com.insurance.policy.infrastructure.adapter.in.api;

import com.insurance.policy.domain.entity.Person;
import com.insurance.policy.domain.entity.Policy;
import com.insurance.policy.domain.entity.PolicyType;
import com.insurance.policy.domain.port.in.service.PolicyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = PolicyController.class)
class PolicyControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private PolicyService policyService;

    private Policy policy;

    @BeforeEach
    void setUp() {
        policy = Policy.builder()
                .id(1L)
                .customerId(100L)
                .type(PolicyType.VIDA)
                .beneficiaries(List.of(
                        Person.builder().firstName("Bene").lastName("One").build()
                ))
                .build();
    }

    @Test
    void create_Success() {
        when(policyService.createPolicy(any(Policy.class), anyString())).thenReturn(Mono.just(policy));

        webTestClient.post()
                .uri("/api/policies")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(policy)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.message").isEqualTo("success")
                .jsonPath("$.data.type").isEqualTo("VIDA");
    }

    @Test
    void findByCustomer_Success() {
        when(policyService.findByCustomerId(anyLong(), anyString())).thenReturn(Flux.just(policy));

        webTestClient.get()
                .uri("/api/policies/customer/100")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.message").isEqualTo("success")
                .jsonPath("$.data[0].id").isEqualTo(1);
    }

    @Test
    void findById_Success() {
        when(policyService.findById(anyLong(), anyString())).thenReturn(Mono.just(policy));

        webTestClient.get()
                .uri("/api/policies/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.message").isEqualTo("success")
                .jsonPath("$.data.id").isEqualTo(1);
    }

    @Test
    void getBeneficiaries_Success() {
        Person person = Person.builder().firstName("Bene").lastName("One").build();
        when(policyService.findBeneficiariesByPolicyId(anyLong(), anyString())).thenReturn(Flux.just(person));

        webTestClient.get()
                .uri("/api/policies/1/beneficiaries")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.message").isEqualTo("success")
                .jsonPath("$.data[0].firstName").isEqualTo("Bene");
    }
}

