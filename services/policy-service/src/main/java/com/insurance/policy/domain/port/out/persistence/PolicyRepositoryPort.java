package com.insurance.policy.domain.port.out.persistence;

import com.insurance.policy.domain.entity.Policy;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Puerto de salida para la persistencia de pólizas de seguros.
 * <p>
 * Esta interfaz define el contrato para las operaciones de persistencia en el patrón
 * de Arquitectura Hexagonal. Representa las operaciones que el dominio necesita de la
 * infraestructura de persistencia, sin depender de ninguna tecnología específica.
 * </p>
 * <p>
 * <b>Principios:</b>
 * </p>
 * <ul>
 *   <li>Define operaciones CRUD y consultas específicas del dominio de pólizas</li>
 *   <li>Independiente de la tecnología de persistencia (puede ser R2DBC, MongoDB, etc.)</li>
 *   <li>Todas las operaciones son reactivas (retornan Mono o Flux)</li>
 *   <li>No contiene lógica de negocio, solo operaciones de persistencia</li>
 * </ul>
 * <p>
 * <b>Implementación:</b> Los adaptadores de persistencia deben implementar esta interfaz.
 * </p>
 *
 * @author Anthony Colmenares
 * @version 1.0.0
 * @since 2026-03-02
 * @see Policy
 */
public interface PolicyRepositoryPort {

    /**
     * Guarda o actualiza una póliza en el sistema de persistencia.
     *
     * @param policy la póliza a guardar
     * @return un {@link Mono} que emite la póliza guardada con su ID asignado
     */
    Mono<Policy> save(Policy policy);

    /**
     * Busca todas las pólizas asociadas a un cliente específico.
     *
     * @param customerId el identificador del cliente
     * @return un {@link Flux} que emite todas las pólizas del cliente, o vacío si no tiene ninguna
     */
    Flux<Policy> findByCustomerId(Long customerId);

    /**
     * Busca una póliza por su identificador único.
     *
     * @param id el identificador de la póliza
     * @return un {@link Mono} que emite la póliza encontrada, o vacío si no existe
     */
    Mono<Policy> findById(Long id);

    /**
     * Verifica si un cliente ya tiene una póliza de un tipo específico.
     * <p>
     * Útil para validar reglas de negocio como "solo una póliza de VIDA por cliente".
     * </p>
     *
     * @param customerId el identificador del cliente
     * @param type el tipo de póliza a verificar (ej: "VIDA", "VEHICULO", "SALUD")
     * @return un {@link Mono} que emite {@code true} si existe, {@code false} en caso contrario
     */
    Mono<Boolean> existsByCustomerIdAndType(Long customerId, String type);
}
