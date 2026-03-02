package com.insurance.policy.infrastructure.adapter.in.api.exception;

import com.insurance.policy.domain.exception.BusinessException;
import com.insurance.policy.infrastructure.adapter.in.api.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

import static com.insurance.policy.infrastructure.adapter.in.api.util.TransactionIdGenerator.getTransactionIdByExchange;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public Mono<ResponseEntity<ApiResponse<Void>>> handleBusinessException(BusinessException ex,
                                                                           ServerWebExchange exchange) {
        return Mono.just(
                ResponseEntity.status(ex.getCode())
                        .body(ApiResponse.error(ex.getMessage(),
                                ex.getCode(),
                                getTransactionIdByExchange(exchange)))
        );
    }

    @ExceptionHandler(RuntimeException.class)
    public Mono<ResponseEntity<ApiResponse<Void>>> handleRuntimeException(RuntimeException ex,
                                                                          ServerWebExchange exchange) {
        return Mono.just(
                ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.error(ex.getMessage(),
                                HttpStatus.BAD_REQUEST.value(),
                                getTransactionIdByExchange(exchange)))
        );
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ResponseEntity<ApiResponse<Void>>> handleBindException(WebExchangeBindException ex,
                                                                       ServerWebExchange exchange) {
        String errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return Mono.just(
                ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.error("Validation Error: " + errors,
                                HttpStatus.BAD_REQUEST.value(),
                                getTransactionIdByExchange(exchange)))
        );
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<ApiResponse<Void>>> handleGeneralException(Exception ex,
                                                                          ServerWebExchange exchange) {
        return Mono.just(
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(ApiResponse.error("An unexpected error occurred",
                                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                getTransactionIdByExchange(exchange)
                        )));
    }
}
