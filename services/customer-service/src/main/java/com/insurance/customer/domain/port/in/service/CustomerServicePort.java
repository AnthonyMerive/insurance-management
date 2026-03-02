package com.insurance.customer.domain.port.in.service;

import com.insurance.customer.domain.entity.Customer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Puerto de entrada para el caso de uso de gestión de clientes.
 */
public interface CustomerServicePort {

    Mono<Customer> create(Customer customer, String transactionId);

    Mono<Customer> findById(Long id, String transactionId);

    Flux<Customer> findAll(String transactionId);

    Mono<Customer> update(Long id, Customer customer, String transactionId);

    Mono<Void> delete(Long id, String transactionId);
}
