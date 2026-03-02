package com.insurance.customer.infrastructure.adapter.out.traceability;

import com.google.gson.*;
import com.insurance.customer.domain.exception.BusinessException;
import com.insurance.customer.domain.port.out.traceability.TraceabilityPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.insurance.customer.infrastructure.adapter.out.traceability.data.LogEnum.*;


@Component
@RequiredArgsConstructor
@Log
public class TraceabilityAdapter implements TraceabilityPort {

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>) (src, typeOfSrc, context) ->
                    new JsonPrimitive(src.format(DateTimeFormatter.ISO_LOCAL_DATE)))
            .registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>) (json, typeOfT, context) ->
                    LocalDate.parse(json.getAsString(), DateTimeFormatter.ISO_LOCAL_DATE))
            .create();

    @Override
    public <T> Mono<T> traceIn(T data,
                               String operation,
                               String transactionId) {

        var stringData = gson.toJson(data);
        var logMessage = String.format(LOG_INPUT_FORMAT.getValue(), operation, transactionId, SUCCESS_MESSAGE.getValue(), stringData);

        log.info(logMessage);
        return Mono.just(data);
    }

    @Override
    public <T> Mono<T> traceOut(T data,
                                String operation,
                                String transactionId) {

        var stringData = gson.toJson(data);
        var logMessage = String.format(LOG_OUTPUT_FORMAT.getValue(), operation, transactionId, SUCCESS_MESSAGE.getValue(), stringData);

        log.info(logMessage);
        return Mono.just(data);
    }

    @Override
    public Mono<Void> traceError(Throwable error,
                                 String operation,
                                 String transactionId) {

        return error instanceof BusinessException
                ? logOutErrorBrule(error, operation, transactionId)
                : logOutErrorTechnical(error, operation, transactionId);
    }

    private Mono<Void>logOutErrorBrule(Throwable error,
                                        String operation,
                                        String transactionId) {

        var logMessage = String.format(LOG_BUSINESS_FORMAT.getValue(), operation, transactionId, error.getMessage());

        log.warning(logMessage);
        return Mono.error(error);
    }

    private Mono<Void> logOutErrorTechnical(Throwable error,
                                             String operation,
                                             String transactionId) {

        var logMessage = String.format(LOG_TECHNICAL_FORMAT.getValue(), operation, transactionId, error.getMessage());

        log.severe(logMessage);
        return Mono.error(error);
    }
}
