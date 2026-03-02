package com.insurance.policy.infrastructure.adapter.out.traceability.data;

import lombok.Getter;

@Getter
public enum LogEnum {

    LOG_INPUT_FORMAT("[INPUT] [OPERATION: %s] [TRANSACTION_ID: %s] [MESSAGE: %s] DATA: %s"),
    LOG_OUTPUT_FORMAT("[OUTPUT] [OPERATION: %s] [TRANSACTION_ID: %s] [MESSAGE: %s] DATA: %s"),
    LOG_BUSINESS_FORMAT("[BRULE_ERROR] [OPERATION: %s] [TRANSACTION_ID: %s] [MESSAGE: %s]"),
    LOG_TECHNICAL_FORMAT("[TECHNICAL_ERROR] [OPERATION: %s] [TRANSACTION_ID: %s] [MESSAGE: %s]"),
    SUCCESS_MESSAGE("Exitoso");

    private final String value;

    LogEnum(String value) {
        this.value = value;
    }

}
