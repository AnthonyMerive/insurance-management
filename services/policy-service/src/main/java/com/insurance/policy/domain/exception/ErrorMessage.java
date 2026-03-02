package com.insurance.policy.domain.exception;

import lombok.Getter;

@Getter
public enum ErrorMessage {
    POLICY_NOT_FOUND("Póliza no encontrada", 404),
    CUSTOMER_ALREADY_HAS_LIFE_POLICY("Cliente ya tiene una póliza de Vida", 400),
    LIFE_POLICY_MAX_BENEFICIARIES("Vida solo permite hasta 2 beneficiarios", 400),
    UNEXPECTED_ERROR("Ha ocurrido un error inesperado", 500);

    private final String message;
    private final int code;

    ErrorMessage(String message, int code) {
        this.message = message;
        this.code = code;
    }
}

