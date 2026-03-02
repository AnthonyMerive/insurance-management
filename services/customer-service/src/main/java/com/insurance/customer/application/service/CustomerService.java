package com.insurance.customer.application.service;

import com.insurance.customer.domain.entity.Customer;
import com.insurance.customer.domain.exception.BusinessException;
import com.insurance.customer.domain.exception.ErrorMessage;
import com.insurance.customer.domain.port.in.service.CustomerServicePort;
import com.insurance.customer.domain.port.out.persistence.CustomerRepositoryPort;
import com.insurance.customer.domain.port.out.traceability.TraceabilityPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * Servicio que implementa la lógica de negocio para Clientes.
 */
@Service
@RequiredArgsConstructor
public class CustomerService implements CustomerServicePort {

    private final CustomerRepositoryPort repository;
    private final TraceabilityPort traceability;

    private static final String CUSTOMER_ID = "customerId";

    private static final String OPERATION_CREATE = "createCustomer";
    private static final String OPERATION_FIND_BY_ID = "findCustomerById";
    private static final String OPERATION_FIND_ALL = "findAllCustomers";
    private static final String OPERATION_UPDATE = "updateCustomer";
    private static final String OPERATION_DELETE = "deleteCustomer";

    @Override
    public Mono<Customer> create(Customer customer, String transactionId) {

        return traceability.traceIn(customer, OPERATION_CREATE, transactionId)
                .flatMap(repository::save)
                .flatMap(saved -> traceability.traceOut(saved, OPERATION_CREATE, transactionId))
                .doOnError(throwable -> traceability.traceError(throwable, OPERATION_CREATE, transactionId));
    }

    @Override
    public Mono<Customer> findById(Long id, String transactionId) {

        return traceability.traceIn(Map.of(CUSTOMER_ID, id), OPERATION_FIND_BY_ID, transactionId)
                .flatMap(request -> validateCustomerExists(id))
                .flatMap(customer -> traceability.traceOut(customer, OPERATION_FIND_BY_ID, transactionId))
                .doOnError(throwable -> traceability.traceError(throwable, OPERATION_FIND_BY_ID, transactionId));
    }

    @Override
    public Flux<Customer> findAll(String transactionId) {

        return traceability.traceIn(new Object(), OPERATION_FIND_ALL, transactionId)
                .flatMapMany(unused -> repository.findAll())
                .flatMap(customer -> traceability.traceOut(customer, OPERATION_FIND_ALL, transactionId))
                .doOnError(throwable -> traceability.traceError(throwable, OPERATION_FIND_ALL, transactionId));
    }

    @Override
    public Mono<Customer> update(Long id, Customer customer, String transactionId) {

        return traceability.traceIn(customer, OPERATION_UPDATE, transactionId)
                .flatMap(unused -> validateCustomerExists(id))
                .flatMap(unused -> updateCustomer(id, customer))
                .flatMap(updated -> traceability.traceOut(updated, OPERATION_UPDATE, transactionId))
                .doOnError(throwable -> traceability.traceError(throwable, OPERATION_UPDATE, transactionId));
    }

    private Mono<Customer> updateCustomer(Long id, Customer customer) {
        customer.setId(id);
        return repository.save(customer);
    }

    @Override
    public Mono<Void> delete(Long id, String transactionId) {

        return traceability.traceIn(Map.of(CUSTOMER_ID, id), OPERATION_DELETE, transactionId)
                .flatMap(unused -> validateCustomerExists(id))
                .flatMap(unused -> repository.deleteById(id).thenReturn(id))
                .flatMap(unused -> traceability.traceOut(Map.of(CUSTOMER_ID, id), OPERATION_DELETE, transactionId))
                .doOnError(throwable -> traceability.traceError(throwable, OPERATION_DELETE, transactionId))
                .then();
    }

    private Mono<Customer> validateCustomerExists(Long id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new BusinessException(ErrorMessage.CUSTOMER_NOT_FOUND)));
    }
}
