package com.insurance.customer.domain.port.out.persistence;

import com.insurance.customer.domain.entity.Customer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Puerto de salida para la persistencia de clientes.
 */
public interface CustomerRepositoryPort {
    Mono<Customer> save(Customer customer);
    Mono<Customer> findById(Long id);
    Flux<Customer> findAll();
    Mono<Void> deleteById(Long id);
}
