package com.insurance.policy.infrastructure.adapter.out.persistence.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.insurance.policy.domain.entity.Policy;
import com.insurance.policy.domain.entity.PolicyType;
import com.insurance.policy.infrastructure.adapter.out.persistence.data.PolicyEntity;

public interface PolicyPersistenceMapper {

    default PolicyEntity toData(Policy policy, ObjectMapper objectMapper) {
        try {
            return PolicyEntity.builder()
                    .id(policy.getId())
                    .customerId(policy.getCustomerId())
                    .type(policy.getType().name())
                    .extraData(objectMapper.writeValueAsString(policy))
                    .build();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    default Policy toDomain(PolicyEntity entity, ObjectMapper objectMapper) {
        try {
            Policy policy = objectMapper.readValue(entity.getExtraData(), Policy.class);
            policy.setId(entity.getId());
            policy.setCustomerId(entity.getCustomerId());
            policy.setType(PolicyType.valueOf(entity.getType()));
            return policy;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
