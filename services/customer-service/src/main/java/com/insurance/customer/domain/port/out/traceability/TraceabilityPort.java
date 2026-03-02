package com.insurance.customer.domain.port.out.traceability;

import reactor.core.publisher.Mono;

public interface TraceabilityPort {

    <T> Mono<T> traceIn(T data, String operation, String transactionId);

    <T> Mono<T> traceOut(T data, String operation, String transactionId);

    Mono<Void> traceError(Throwable throwable, String operation, String transactionId);
}