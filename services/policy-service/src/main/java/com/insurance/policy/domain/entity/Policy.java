package com.insurance.policy.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Policy {
    private Long id;
    private Long customerId;
    private PolicyType type;
    private List<Person> beneficiaries; // Para Vida y Salud
    private List<String> insuredVehicles; // Para Vehículo
    private Integer extraParents; // Para Salud
    private Integer extraChildren; // Para Salud
    private Boolean hasSpouse; // Para Salud
}

