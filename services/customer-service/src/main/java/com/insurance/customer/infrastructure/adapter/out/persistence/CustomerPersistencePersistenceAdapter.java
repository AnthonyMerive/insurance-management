package com.insurance.customer.infrastructure.adapter.out.persistence;

import com.insurance.customer.domain.entity.Customer;
import com.insurance.customer.domain.port.out.persistence.CustomerRepositoryPort;
import com.insurance.customer.infrastructure.adapter.out.persistence.mapper.CustomerPersistenceMapper;
import com.insurance.customer.infrastructure.adapter.out.persistence.repository.CustomerR2dbcRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Adaptador de salida para persistencia usando R2DBC y H2.
 */
@Component
@RequiredArgsConstructor
public class CustomerPersistencePersistenceAdapter implements CustomerRepositoryPort, CustomerPersistenceMapper {

    private final CustomerR2dbcRepository repository;

    @Override
    public Mono<Customer> save(Customer customer) {
        return repository.save(toData(customer))
                .map(this::toDomain);
    }

    @Override
    public Mono<Customer> findById(Long id) {
        return repository.findById(id)
                .map(this::toDomain);
    }

    @Override
    public Flux<Customer> findAll() {
        return repository.findAll()
                .map(this::toDomain);
    }

    @Override
    public Mono<Void> deleteById(Long id) {
        return repository.deleteById(id);
    }
}