package com.insurance.policy.infrastructure.adapter.in.api;

import com.insurance.policy.domain.entity.Person;
import com.insurance.policy.domain.entity.Policy;
import com.insurance.policy.domain.port.in.service.PolicyService;
import com.insurance.policy.infrastructure.adapter.in.api.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.insurance.policy.infrastructure.adapter.in.api.util.TransactionIdGenerator.getTransactionIdByExchange;

/**
 * Controlador REST reactivo para la gestión de pólizas de seguros.
 * <p>
 * Este controlador expone los endpoints HTTP para realizar operaciones sobre pólizas de seguros.
 * Soporta tres tipos de pólizas: VIDA, VEHICULO y SALUD, cada una con sus reglas de negocio
 * específicas. Implementa el patrón de Arquitectura Hexagonal como adaptador de entrada (API REST).
 * </p>
 * <p>
 * <b>Base URL:</b> {@code /api/policies}
 * </p>
 * <p>
 * <b>Tipos de Póliza Soportados:</b>
 * </p>
 * <ul>
 *   <li><b>VIDA:</b> Solo una por cliente, requiere beneficiarios (máx. 2)</li>
 *   <li><b>VEHICULO:</b> Requiere lista de vehículos asegurados</li>
 *   <li><b>SALUD:</b> Puede incluir familiares adicionales (padres, hijos, cónyuge)</li>
 * </ul>
 * <p>
 * <b>Formato de Respuesta:</b> Todas las respuestas siguen el formato {@link ApiResponse}
 * que incluye transactionId, message, code y data.
 * </p>
 *
 * @author Anthony Colmenares
 * @version 1.0.0
 * @since 2026-03-02
 * @see PolicyService
 * @see ApiResponse
 * @see Policy
 */
@RestController
@RequestMapping("/api/policies")
@RequiredArgsConstructor
public class PolicyController {

    /** Puerto de servicio para la lógica de negocio de pólizas. */
    private final PolicyService policyService;

    /**
     * Crea una nueva póliza de seguro.
     * <p>
     * <b>Endpoint:</b> {@code POST /api/policies}
     * </p>
     * <p>
     * <b>Validaciones según tipo:</b>
     * </p>
     * <ul>
     *   <li><b>VIDA:</b> Cliente no debe tener otra póliza de vida, máx. 2 beneficiarios</li>
     *   <li><b>VEHICULO:</b> Debe incluir al menos un vehículo asegurado</li>
     *   <li><b>SALUD:</b> Validaciones estándar aplicadas</li>
     * </ul>
     * <p>
     * <b>Ejemplo de Request (Póliza VIDA):</b>
     * </p>
     * <pre>
     * {
     *   "customerId": 1,
     *   "type": "VIDA",
     *   "beneficiaries": [
     *     {
     *       "firstName": "Luis",
     *       "lastName": "Gomez",
     *       "dni": "12345678"
     *     }
     *   ]
     * }
     * </pre>
     *
     * @param policy la póliza a crear con todos sus datos
     * @param exchange el intercambio de servidor web para obtener el transactionId
     * @return {@link ResponseEntity} con código 201 (CREATED) y la póliza creada
     * @throws com.insurance.policy.domain.exception.BusinessException si se violan reglas de negocio
     */
    @PostMapping
    public Mono<ResponseEntity<ApiResponse<Policy>>> create(@RequestBody Policy policy, ServerWebExchange exchange) {
        String txId = getTransactionIdByExchange(exchange);
        return policyService.createPolicy(policy, txId)
                .map(response -> handleSuccess(ApiResponse.success(response, txId),
                        HttpStatus.CREATED));
    }

    /**
     * Obtiene todas las pólizas asociadas a un cliente específico.
     * <p>
     * <b>Endpoint:</b> {@code GET /api/policies/customer/{customerId}}
     * </p>
     * <p>
     * Retorna todas las pólizas (de cualquier tipo) que pertenezcan al cliente.
     * Útil para visualizar todas las coberturas de un cliente.
     * </p>
     * <p>
     * <b>Ejemplo:</b> {@code GET /api/policies/customer/1}
     * </p>
     *
     * @param customerId el identificador del cliente
     * @param exchange el intercambio de servidor web para obtener el transactionId
     * @return {@link ResponseEntity} con código 200 (OK) y la lista de pólizas del cliente
     * @throws com.insurance.policy.domain.exception.BusinessException si el cliente no tiene pólizas
     */
    @GetMapping("/customer/{customerId}")
    public Mono<ResponseEntity<ApiResponse<List<Policy>>>> findByCustomer(@PathVariable Long customerId, ServerWebExchange exchange) {
        String txId = getTransactionIdByExchange(exchange);
        return policyService.findByCustomerId(customerId, txId)
                .collectList()
                .map(response -> handleSuccess(ApiResponse.success(response, txId),
                        HttpStatus.OK));
    }

    /**
     * Busca una póliza específica por su identificador.
     * <p>
     * <b>Endpoint:</b> {@code GET /api/policies/{id}}
     * </p>
     * <p>
     * Retorna los detalles completos de la póliza incluyendo todos sus atributos
     * según el tipo (beneficiarios, vehículos, familiares adicionales, etc.).
     * </p>
     * <p>
     * <b>Ejemplo:</b> {@code GET /api/policies/1}
     * </p>
     *
     * @param id el identificador único de la póliza
     * @param exchange el intercambio de servidor web para obtener el transactionId
     * @return {@link ResponseEntity} con código 200 (OK) y la póliza encontrada
     * @throws com.insurance.policy.domain.exception.BusinessException si la póliza no existe (404)
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<ApiResponse<Policy>>> findById(@PathVariable Long id, ServerWebExchange exchange) {
        String txId = getTransactionIdByExchange(exchange);
        return policyService.findById(id, txId)
                .map(response -> handleSuccess(ApiResponse.success(response, txId),
                        HttpStatus.OK));
    }

    /**
     * Obtiene la lista de beneficiarios de una póliza específica.
     * <p>
     * <b>Endpoint:</b> {@code GET /api/policies/{id}/beneficiaries}
     * </p>
     * <p>
     * Este endpoint es principalmente útil para pólizas de tipo VIDA.
     * Si la póliza no tiene beneficiarios, retorna un array vacío.
     * </p>
     * <p>
     * <b>Ejemplo:</b> {@code GET /api/policies/1/beneficiaries}
     * </p>
     * <p>
     * <b>Ejemplo de Respuesta:</b>
     * </p>
     * <pre>
     * {
     *   "transactionId": "uuid",
     *   "message": "success",
     *   "code": 200,
     *   "data": [
     *     {
     *       "firstName": "Luis",
     *       "lastName": "Gomez",
     *       "dni": "12345678"
     *     }
     *   ]
     * }
     * </pre>
     *
     * @param id el identificador de la póliza
     * @param exchange el intercambio de servidor web para obtener el transactionId
     * @return {@link ResponseEntity} con código 200 (OK) y la lista de beneficiarios
     * @throws com.insurance.policy.domain.exception.BusinessException si la póliza no existe (404)
     */
    @GetMapping("/{id}/beneficiaries")
    public Mono<ResponseEntity<ApiResponse<List<Person>>>> getBeneficiaries(@PathVariable Long id, ServerWebExchange exchange) {
        String txId = getTransactionIdByExchange(exchange);
        return policyService.findBeneficiariesByPolicyId(id, txId)
                .collectList()
                .map(response -> handleSuccess(ApiResponse.success(response, txId),
                        HttpStatus.OK));
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