package com.insurance.policy.infrastructure.adapter.in.api.filter;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class TransactionIdGenerationFilter implements WebFilter {

    public static final String CONTEXT_KEY = "transactionId";

    @Override
    public @NonNull Mono<Void> filter(@NonNull ServerWebExchange exchange,
                                      @NonNull WebFilterChain chain) {
        String txId = UUID.randomUUID().toString();
        exchange.getAttributes().put(CONTEXT_KEY, txId);
        return chain.filter(exchange);
    }
}

