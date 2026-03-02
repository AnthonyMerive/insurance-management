package com.insurance.policy.infrastructure.adapter.out.persistence.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("policies")
public class PolicyEntity {
    @Id
    private Long id;
    private Long customerId;
    private String type;
    private String extraData; // JSON para beneficiarios, vehículos, etc.
}
