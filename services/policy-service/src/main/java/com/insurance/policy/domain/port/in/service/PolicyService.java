package com.insurance.policy.domain.port.in.service;

import com.insurance.policy.domain.entity.Person;
import com.insurance.policy.domain.entity.Policy;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Puerto de entrada para el caso de uso de gestión de pólizas de seguros.
 * <p>
 * Esta interfaz define el contrato para las operaciones de negocio relacionadas con pólizas
 * en el patrón de Arquitectura Hexagonal. Representa los casos de uso que el sistema expone
 * a los adaptadores de entrada (como controladores REST).
 * </p>
 * <p>
 * <b>Tipos de Póliza Soportados:</b>
 * </p>
 * <ul>
 *   <li><b>VIDA:</b> Con reglas específicas (una por cliente, beneficiarios obligatorios)</li>
 *   <li><b>VEHICULO:</b> Con lista de vehículos asegurados</li>
 *   <li><b>SALUD:</b> Con opciones de cobertura familiar</li>
 * </ul>
 * <p>
 * <b>Principios:</b>
 * </p>
 * <ul>
 *   <li>Define el "qué" pero no el "cómo" (responsabilidad de la implementación)</li>
 *   <li>Independiente de la infraestructura y tecnologías específicas</li>
 *   <li>Todas las operaciones son reactivas (retornan Mono o Flux)</li>
 *   <li>Incluye transactionId para trazabilidad de operaciones</li>
 *   <li>Aplica reglas de negocio específicas por tipo de póliza</li>
 * </ul>
 *
 * @author Anthony Colmenares
 * @version 1.0.0
 * @since 2026-03-02
 * @see Policy
 * @see Person
 */
public interface PolicyService {

    /**
     * Crea una nueva póliza de seguro aplicando las reglas de negocio según el tipo.
     *
     * @param policy la póliza a crear con todos sus datos, no debe ser {@code null}
     * @param transactionId identificador único de transacción para trazabilidad
     * @return un {@link Mono} que emite la póliza creada con su ID asignado
     */
    Mono<Policy> createPolicy(Policy policy, String transactionId);

    /**
     * Busca todas las pólizas asociadas a un cliente específico.
     *
     * @param customerId el identificador del cliente
     * @param transactionId identificador único de transacción para trazabilidad
     * @return un {@link Flux} que emite todas las pólizas del cliente
     */
    Flux<Policy> findByCustomerId(Long customerId, String transactionId);

    /**
     * Busca una póliza por su identificador único.
     *
     * @param id el identificador de la póliza
     * @param transactionId identificador único de transacción para trazabilidad
     * @return un {@link Mono} que emite la póliza encontrada
     */
    Mono<Policy> findById(Long id, String transactionId);

    /**
     * Obtiene la lista de beneficiarios de una póliza específica.
     * <p>
     * Este método es principalmente útil para pólizas de tipo VIDA.
     * </p>
     *
     * @param id el identificador de la póliza
     * @param transactionId identificador único de transacción para trazabilidad
     * @return un {@link Flux} que emite todos los beneficiarios de la póliza
     */
    Flux<Person> findBeneficiariesByPolicyId(Long id, String transactionId);
}
