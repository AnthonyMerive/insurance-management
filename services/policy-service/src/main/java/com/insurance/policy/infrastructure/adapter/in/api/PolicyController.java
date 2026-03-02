package com.insurance.policy.infrastructure.adapter.in.api;

import com.insurance.policy.domain.entity.Person;
import com.insurance.policy.domain.entity.Policy;
import com.insurance.policy.domain.port.in.service.PolicyService;
import com.insurance.policy.infrastructure.adapter.in.api.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.insurance.policy.infrastructure.adapter.in.api.util.TransactionIdGenerator.getTransactionIdByExchange;

@RestController
@RequestMapping("/api/policies")
@RequiredArgsConstructor
public class PolicyController {

    private final PolicyService policyService;

    @PostMapping
    public Mono<ResponseEntity<ApiResponse<Policy>>> create(@RequestBody Policy policy, ServerWebExchange exchange) {
        String txId = getTransactionIdByExchange(exchange);
        return policyService.createPolicy(policy, txId)
                .map(response -> handleSuccess(ApiResponse.success(response, txId),
                        HttpStatus.CREATED));
    }

    @GetMapping("/customer/{customerId}")
    public Mono<ResponseEntity<ApiResponse<List<Policy>>>> findByCustomer(@PathVariable Long customerId, ServerWebExchange exchange) {
        String txId = getTransactionIdByExchange(exchange);
        return policyService.findByCustomerId(customerId, txId)
                .collectList()
                .map(response -> handleSuccess(ApiResponse.success(response, txId),
                        HttpStatus.OK));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<ApiResponse<Policy>>> findById(@PathVariable Long id, ServerWebExchange exchange) {
        String txId = getTransactionIdByExchange(exchange);
        return policyService.findById(id, txId)
                .map(response -> handleSuccess(ApiResponse.success(response, txId),
                        HttpStatus.OK));
    }

    @GetMapping("/{id}/beneficiaries")
    public Mono<ResponseEntity<ApiResponse<List<Person>>>> getBeneficiaries(@PathVariable Long id, ServerWebExchange exchange) {
        String txId = getTransactionIdByExchange(exchange);
        return policyService.findBeneficiariesByPolicyId(id, txId)
                .collectList()
                .map(response -> handleSuccess(ApiResponse.success(response, txId),
                        HttpStatus.OK));
    }

    private <T> ResponseEntity<T> handleSuccess(T body, HttpStatus status) {
        return ResponseEntity.status(status).body(body);
    }
}