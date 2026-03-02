package com.insurance.customer.infrastructure.adapter.in.api;

import com.insurance.customer.domain.entity.Customer;
import com.insurance.customer.domain.port.in.service.CustomerServicePort;
import com.insurance.customer.infrastructure.adapter.in.api.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.insurance.customer.infrastructure.adapter.in.api.util.TransactionIdGenerator.getTransactionIdByExchange;

/**
 * Controlador REST reactivo para la gestión de clientes.
 * <p>
 * Este controlador expone los endpoints HTTP para realizar operaciones CRUD sobre clientes.
 * Implementa el patrón de Arquitectura Hexagonal como adaptador de entrada (API REST).
 * Todas las respuestas incluyen un {@code transactionId} único para trazabilidad.
 * </p>
 * <p>
 * <b>Base URL:</b> {@code /api/customers}
 * </p>
 * <p>
 * <b>Formato de Respuesta:</b> Todas las respuestas siguen el formato {@link ApiResponse}
 * que incluye transactionId, message, code y data.
 * </p>
 *
 * @author Anthony Colmenares
 * @version 1.0.0
 * @since 2026-03-02
 * @see CustomerServicePort
 * @see ApiResponse
 */
@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    /** Puerto de servicio para la lógica de negocio de clientes. */
    private final CustomerServicePort customerServicePort;

    /**
     * Crea un nuevo cliente en el sistema.
     * <p>
     * <b>Endpoint:</b> {@code POST /api/customers}
     * </p>
     * <p>
     * <b>Request Body:</b> JSON con los datos del cliente (firstName, lastName, email, etc.)
     * </p>
     * <p>
     * <b>Ejemplo de Request:</b>
     * </p>
     * <pre>
     * {
     *   "firstName": "Ana",
     *   "lastName": "Perez",
     *   "email": "ana.perez@example.com",
     *   "phoneNumber": "+57-300-000-0000",
     *   "birthDate": "1990-01-15"
     * }
     * </pre>
     *
     * @param customer el cliente a crear con todos sus datos
     * @param exchange el intercambio de servidor web para obtener el transactionId
     * @return {@link ResponseEntity} con código 201 (CREATED) y el cliente creado en el cuerpo
     */
    @PostMapping
    public Mono<ResponseEntity<ApiResponse<Customer>>> create(@RequestBody Customer customer,
                                                              ServerWebExchange exchange) {
        String txId = getTransactionIdByExchange(exchange);
        return customerServicePort.create(customer, txId)
                .map(response -> handleSuccess(ApiResponse.success(response, txId),
                        HttpStatus.CREATED));
    }

    /**
     * Obtiene la lista de todos los clientes registrados.
     * <p>
     * <b>Endpoint:</b> {@code GET /api/customers}
     * </p>
     * <p>
     * Retorna un array con todos los clientes del sistema. Si no hay clientes,
     * retorna un array vacío.
     * </p>
     *
     * @param exchange el intercambio de servidor web para obtener el transactionId
     * @return {@link ResponseEntity} con código 200 (OK) y la lista de clientes
     */
    @GetMapping
    public Mono<ResponseEntity<ApiResponse<List<Customer>>>> findAll(ServerWebExchange exchange) {
        String txId = getTransactionIdByExchange(exchange);
        return customerServicePort.findAll(txId)
                .collectList()
                .map(response -> handleSuccess(ApiResponse.success(response, txId),
                        HttpStatus.OK));
    }

    /**
     * Busca un cliente específico por su identificador.
     * <p>
     * <b>Endpoint:</b> {@code GET /api/customers/{id}}
     * </p>
     * <p>
     * <b>Ejemplo:</b> {@code GET /api/customers/1}
     * </p>
     *
     * @param id el identificador único del cliente
     * @param exchange el intercambio de servidor web para obtener el transactionId
     * @return {@link ResponseEntity} con código 200 (OK) y el cliente encontrado
     * @throws com.insurance.customer.domain.exception.BusinessException si el cliente no existe (404)
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<ApiResponse<Customer>>> findById(@PathVariable Long id,
                                                                ServerWebExchange exchange) {
        String txId = getTransactionIdByExchange(exchange);
        return customerServicePort.findById(id, txId)
                .map(response -> handleSuccess(ApiResponse.success(response, txId),
                        HttpStatus.OK));
    }

    /**
     * Actualiza la información de un cliente existente.
     * <p>
     * <b>Endpoint:</b> {@code PUT /api/customers/{id}}
     * </p>
     * <p>
     * El ID en la URL prevalece sobre cualquier ID en el body. Se actualizan
     * todos los campos proporcionados en el request body.
     * </p>
     * <p>
     * <b>Ejemplo de Request:</b>
     * </p>
     * <pre>
     * PUT /api/customers/1
     * {
     *   "firstName": "Ana",
     *   "lastName": "Perez Martinez",
     *   "email": "ana.updated@example.com",
     *   "phoneNumber": "+57-300-111-1111",
     *   "birthDate": "1990-01-15"
     * }
     * </pre>
     *
     * @param id el identificador del cliente a actualizar
     * @param customer los nuevos datos del cliente
     * @param exchange el intercambio de servidor web para obtener el transactionId
     * @return {@link ResponseEntity} con código 200 (OK) y el cliente actualizado
     * @throws com.insurance.customer.domain.exception.BusinessException si el cliente no existe (404)
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<ApiResponse<Customer>>> update(@PathVariable Long id,
                                                              @RequestBody Customer customer,
                                                              ServerWebExchange exchange) {
        String txId = getTransactionIdByExchange(exchange);
        return customerServicePort.update(id, customer, txId)
                .map(response -> handleSuccess(ApiResponse.success(response, txId),
                        HttpStatus.OK));
    }

    /**
     * Elimina un cliente del sistema.
     * <p>
     * <b>Endpoint:</b> {@code DELETE /api/customers/{id}}
     * </p>
     * <p>
     * Esta operación es irreversible. El cliente debe existir previamente.
     * </p>
     * <p>
     * <b>Ejemplo:</b> {@code DELETE /api/customers/1}
     * </p>
     *
     * @param id el identificador del cliente a eliminar
     * @param exchange el intercambio de servidor web para obtener el transactionId
     * @return {@link ResponseEntity} con código 204 (NO_CONTENT) si la eliminación es exitosa
     * @throws com.insurance.customer.domain.exception.BusinessException si el cliente no existe (404)
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<ApiResponse<Object>>> delete(@PathVariable Long id,
                                                            ServerWebExchange exchange) {
        String txId = getTransactionIdByExchange(exchange);
        return customerServicePort.delete(id, txId)
                .thenReturn(handleSuccess(ApiResponse.success(null, txId),
                        HttpStatus.NO_CONTENT));
    }

    /**
     * Método auxiliar para construir respuestas HTTP exitosas.
     * <p>
     * Encapsula la respuesta en un {@link ResponseEntity} con el código de estado apropiado.
     * </p>
     *
     * @param body el cuerpo de la respuesta
     * @param status el código de estado HTTP
     * @param <T> el tipo del cuerpo de la respuesta
     * @return un {@link ResponseEntity} con el cuerpo y estado especificados
     */
    private <T> ResponseEntity<T> handleSuccess(T body, HttpStatus status) {
        return ResponseEntity.status(status).body(body);
    }
}