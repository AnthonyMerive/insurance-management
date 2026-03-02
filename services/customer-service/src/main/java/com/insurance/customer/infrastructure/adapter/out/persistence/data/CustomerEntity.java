package com.insurance.customer.infrastructure.adapter.out.persistence.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("customers")
public class CustomerEntity {
    @Id
    private Long id;
    private String documentType;
    private String documentNumber;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private LocalDate birthDate;
}
