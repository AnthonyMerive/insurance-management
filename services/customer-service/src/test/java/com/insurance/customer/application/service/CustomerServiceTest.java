package com.insurance.customer.application.service;

import com.insurance.customer.domain.entity.Customer;
import com.insurance.customer.domain.exception.BusinessException;
import com.insurance.customer.domain.port.out.persistence.CustomerRepositoryPort;
import com.insurance.customer.domain.port.out.traceability.TraceabilityPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepositoryPort repository;

    @Mock
    private TraceabilityPort traceability;

    @InjectMocks
    private CustomerService customerService;

    private Customer customer;
    private final String txId = "test-transaction-id";

    @BeforeEach
    void setUp() {
        customer = Customer.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .birthDate(LocalDate.of(1990, 1, 1))
                .build();

        lenient().when(traceability.traceIn(any(), anyString(), anyString())).thenAnswer(invocation -> {
            Object arg = invocation.getArgument(0);
            return arg != null ? Mono.just(arg) : Mono.empty();
        });
        lenient().when(traceability.traceOut(any(), anyString(), anyString())).thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));
        lenient().when(traceability.traceError(any(Throwable.class), anyString(), anyString())).thenReturn(Mono.empty());
    }

    @Test
    void create_Success() {
        when(repository.save(any(Customer.class))).thenReturn(Mono.just(customer));

        StepVerifier.create(customerService.create(customer, txId))
                .expectNext(customer)
                .verifyComplete();

        verify(repository).save(customer);
    }

    @Test
    void findById_Success() {
        when(repository.findById(anyLong())).thenReturn(Mono.just(customer));

        StepVerifier.create(customerService.findById(1L, txId))
                .expectNext(customer)
                .verifyComplete();
    }

    @Test
    void findById_NotFound() {
        when(repository.findById(anyLong())).thenReturn(Mono.empty());

        StepVerifier.create(customerService.findById(1L, txId))
                .expectError(BusinessException.class)
                .verify();
    }

    @Test
    void findAll_Success() {
        when(repository.findAll()).thenReturn(Flux.just(customer));

        StepVerifier.create(customerService.findAll(txId))
                .expectNext(customer)
                .verifyComplete();
    }

    @Test
    void update_Success() {
        when(repository.findById(anyLong())).thenReturn(Mono.just(customer));
        when(repository.save(any(Customer.class))).thenReturn(Mono.just(customer));

        StepVerifier.create(customerService.update(1L, customer, txId))
                .expectNext(customer)
                .verifyComplete();
    }

    @Test
    void delete_Success() {
        when(repository.findById(anyLong())).thenReturn(Mono.just(customer));
        when(repository.deleteById(anyLong())).thenReturn(Mono.empty());

        StepVerifier.create(customerService.delete(1L, txId))
                .verifyComplete();
    }
}
