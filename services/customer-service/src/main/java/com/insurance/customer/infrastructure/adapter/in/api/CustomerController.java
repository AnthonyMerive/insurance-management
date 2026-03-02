package com.insurance.customer.infrastructure.adapter.in.api;

import com.insurance.customer.domain.entity.Customer;
import com.insurance.customer.domain.port.in.service.CustomerServicePort;
import com.insurance.customer.infrastructure.adapter.in.api.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.insurance.customer.infrastructure.adapter.in.api.util.TransactionIdGenerator.getTransactionIdByExchange;

/**
 * Controlador REST reactivo para la gestión de clientes.
 */
@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerServicePort customerServicePort;

    @PostMapping
    public Mono<ResponseEntity<ApiResponse<Customer>>> create(@RequestBody Customer customer,
                                                              ServerWebExchange exchange) {
        String txId = getTransactionIdByExchange(exchange);
        return customerServicePort.create(customer, txId)
                .map(response -> handleSuccess(ApiResponse.success(response, txId),
                        HttpStatus.CREATED));
    }

    @GetMapping
    public Mono<ResponseEntity<ApiResponse<List<Customer>>>> findAll(ServerWebExchange exchange) {
        String txId = getTransactionIdByExchange(exchange);
        return customerServicePort.findAll(txId)
                .collectList()
                .map(response -> handleSuccess(ApiResponse.success(response, txId),
                        HttpStatus.OK));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<ApiResponse<Customer>>> findById(@PathVariable Long id,
                                                                ServerWebExchange exchange) {
        String txId = getTransactionIdByExchange(exchange);
        return customerServicePort.findById(id, txId)
                .map(response -> handleSuccess(ApiResponse.success(response, txId),
                        HttpStatus.OK));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<ApiResponse<Customer>>> update(@PathVariable Long id,
                                                              @RequestBody Customer customer,
                                                              ServerWebExchange exchange) {
        String txId = getTransactionIdByExchange(exchange);
        return customerServicePort.update(id, customer, txId)
                .map(response -> handleSuccess(ApiResponse.success(response, txId),
                        HttpStatus.OK));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<ApiResponse<Object>>> delete(@PathVariable Long id,
                                                            ServerWebExchange exchange) {
        String txId = getTransactionIdByExchange(exchange);
        return customerServicePort.delete(id, txId)
                .thenReturn(handleSuccess(ApiResponse.success(null, txId),
                        HttpStatus.NO_CONTENT));
    }

    private <T> ResponseEntity<T> handleSuccess(T body, HttpStatus status) {
        return ResponseEntity.status(status).body(body);
    }
}