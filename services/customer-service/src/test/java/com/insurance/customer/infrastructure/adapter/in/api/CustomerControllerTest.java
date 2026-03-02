package com.insurance.customer.infrastructure.adapter.in.api;

import com.insurance.customer.domain.entity.Customer;
import com.insurance.customer.domain.port.in.service.CustomerServicePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = CustomerController.class)
class CustomerControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private CustomerServicePort customerServicePort;

    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = Customer.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .birthDate(LocalDate.of(1990, 1, 1))
                .build();
    }

    @Test
    void create_Success() {
        when(customerServicePort.create(any(Customer.class), anyString())).thenReturn(Mono.just(customer));

        webTestClient.post()
                .uri("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(customer)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.message").isEqualTo("success")
                .jsonPath("$.data.firstName").isEqualTo("John");
    }

    @Test
    void findAll_Success() {
        when(customerServicePort.findAll(anyString())).thenReturn(Flux.just(customer));

        webTestClient.get()
                .uri("/api/customers")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.message").isEqualTo("success")
                .jsonPath("$.data[0].firstName").isEqualTo("John");
    }

    @Test
    void findById_Success() {
        when(customerServicePort.findById(anyLong(), anyString())).thenReturn(Mono.just(customer));

        webTestClient.get()
                .uri("/api/customers/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.message").isEqualTo("success")
                .jsonPath("$.data.id").isEqualTo(1);
    }

    @Test
    void update_Success() {
        when(customerServicePort.update(anyLong(), any(Customer.class), anyString())).thenReturn(Mono.just(customer));

        webTestClient.put()
                .uri("/api/customers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(customer)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.message").isEqualTo("success")
                .jsonPath("$.data.id").isEqualTo(1);
    }

    @Test
    void delete_Success() {
        when(customerServicePort.delete(anyLong(), anyString())).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/api/customers/1")
                .exchange()
                .expectStatus().isNoContent()
                .expectBody()
                .jsonPath("$.message").isEqualTo("success");
    }
}

