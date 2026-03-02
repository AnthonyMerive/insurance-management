package com.insurance.customer.infrastructure.adapter.in.api.util;

import com.insurance.customer.infrastructure.adapter.in.api.filter.TransactionIdGenerationFilter;
import org.springframework.web.server.ServerWebExchange;

import java.util.UUID;

import static java.util.Objects.nonNull;

public class TransactionIdGenerator {

    private TransactionIdGenerator() {
    }

    public static String getTransactionIdByExchange(ServerWebExchange exchange) {
        return nonNull(exchange.getAttribute(TransactionIdGenerationFilter.CONTEXT_KEY))
                ? exchange.getAttribute(TransactionIdGenerationFilter.CONTEXT_KEY)
                : UUID.randomUUID().toString();
    }
}
