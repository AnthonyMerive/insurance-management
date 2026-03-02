package com.insurance.customer.domain.exception;

import lombok.Getter;

@Getter
public enum ErrorMessage {
    CUSTOMER_NOT_FOUND("Cliente no encontrado", 404),
    INVALID_CUSTOMER_DATA("Datos de cliente inválidos", 400),
    UNEXPECTED_ERROR("Ha ocurrido un error inesperado", 500);

    private final String message;
    private final int code;

    ErrorMessage(String message, int code) {
        this.message = message;
        this.code = code;
    }
}

