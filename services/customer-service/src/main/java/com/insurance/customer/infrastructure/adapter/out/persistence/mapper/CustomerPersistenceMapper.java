package com.insurance.customer.infrastructure.adapter.out.persistence.mapper;

import com.insurance.customer.domain.entity.Customer;
import com.insurance.customer.infrastructure.adapter.out.persistence.data.CustomerEntity;

public interface CustomerPersistenceMapper {

    default CustomerEntity toData(Customer customer) {
        return CustomerEntity.builder()
                .id(customer.getId())
                .documentType(customer.getDocumentType())
                .documentNumber(customer.getDocumentNumber())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .email(customer.getEmail())
                .phone(customer.getPhone())
                .birthDate(customer.getBirthDate())
                .build();
    }

    default Customer toDomain(CustomerEntity entity) {
        return Customer.builder()
                .id(entity.getId())
                .documentType(entity.getDocumentType())
                .documentNumber(entity.getDocumentNumber())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .email(entity.getEmail())
                .phone(entity.getPhone())
                .birthDate(entity.getBirthDate())
                .build();
    }
}
