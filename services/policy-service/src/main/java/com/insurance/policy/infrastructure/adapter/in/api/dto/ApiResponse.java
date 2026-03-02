package com.insurance.policy.infrastructure.adapter.in.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private String transactionId;
    private String message;
    private int code;
    private T data;

    public static <T> ApiResponse<T> success(T data, String transactionId) {
        return ApiResponse.<T>builder()
                .transactionId(transactionId)
                .message("success")
                .code(200)
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> error(String message, int code, String transactionId) {
        return ApiResponse.<T>builder()
                .transactionId(transactionId)
                .message(message)
                .code(code)
                .data(null)
                .build();
    }
}

