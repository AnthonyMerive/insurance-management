package com.insurance.policy.domain.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private final int code;

    public BusinessException(ErrorMessage error) {
        super(error.getMessage());
        this.code = error.getCode();
    }

    public BusinessException(String message) {
        super(message);
        this.code = 400;
    }

    public BusinessException(String message, int code) {
        super(message);
        this.code = code;
    }

}
