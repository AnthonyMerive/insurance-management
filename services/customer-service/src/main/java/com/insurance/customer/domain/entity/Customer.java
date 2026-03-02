package com.insurance.customer.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Representa un Cliente en el sistema de seguros.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    private Long id;
    private String documentType;
    private String documentNumber;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private LocalDate birthDate;
}
