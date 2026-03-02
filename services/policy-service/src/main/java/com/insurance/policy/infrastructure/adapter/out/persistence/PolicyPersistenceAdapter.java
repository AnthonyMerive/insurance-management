package com.insurance.policy.infrastructure.adapter.out.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.insurance.policy.domain.entity.Policy;
import com.insurance.policy.domain.port.out.persistence.PolicyRepositoryPort;
import com.insurance.policy.infrastructure.adapter.out.persistence.mapper.PolicyPersistenceMapper;
import com.insurance.policy.infrastructure.adapter.out.persistence.repository.PolicyR2dbcRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Adaptador de salida para la persistencia de pólizas usando R2DBC y H2.
 * <p>
 * Esta clase implementa el patrón de Arquitectura Hexagonal como adaptador de salida,
 * conectando la lógica de negocio del dominio con la infraestructura de persistencia.
 * Utiliza Spring Data R2DBC para operaciones reactivas sobre la base de datos H2.
 * </p>
 * <p>
 * <b>Características Especiales:</b>
 * </p>
 * <ul>
 *   <li>Almacena pólizas de diferentes tipos (VIDA, VEHICULO, SALUD) en una sola tabla</li>
 *   <li>Utiliza un campo JSON ({@code extraData}) para almacenar atributos específicos por tipo</li>
 *   <li>Usa {@link ObjectMapper} de Jackson para serialización/deserialización JSON</li>
 *   <li>Convierte entre entidades de dominio y entidades de persistencia automáticamente</li>
 * </ul>
 *
 * @author Anthony Colmenares
 * @version 1.0.0
 * @since 2026-03-02
 * @see PolicyRepositoryPort
 * @see PolicyPersistenceMapper
 * @see PolicyR2dbcRepository
 */
@Component
@RequiredArgsConstructor
public class PolicyPersistenceAdapter implements PolicyRepositoryPort, PolicyPersistenceMapper {

    /** Repositorio R2DBC para operaciones de base de datos reactivas. */
    private final PolicyR2dbcRepository repository;

    /** Mapper de Jackson para serializar/deserializar pólizas a JSON. */
    private final ObjectMapper objectMapper;

    /**
     * Guarda o actualiza una póliza en la base de datos.
     * <p>
     * La póliza completa se serializa a JSON y se almacena en el campo {@code extraData}.
     * El tipo y customerId se almacenan también en columnas separadas para facilitar búsquedas.
     * </p>
     * <p>
     * Si la póliza tiene un {@code id}, se actualiza el registro existente.
     * Si no tiene {@code id}, se crea un nuevo registro y se asigna un ID automáticamente.
     * </p>
     *
     * @param policy la póliza a guardar o actualizar
     * @return un {@link Mono} que emite la póliza guardada con su ID asignado
     */
    @Override
    public Mono<Policy> save(Policy policy) {
        return repository.save(toData(policy, objectMapper))
                .map(data -> toDomain(data, objectMapper));
    }

    /**
     * Busca todas las pólizas asociadas a un cliente específico.
     * <p>
     * Retorna todas las pólizas (de cualquier tipo) que pertenezcan al cliente.
     * </p>
     *
     * @param customerId el identificador del cliente
     * @return un {@link Flux} que emite todas las pólizas del cliente, o vacío si no tiene ninguna
     */
    @Override
    public Flux<Policy> findByCustomerId(Long customerId) {
        return repository.findByCustomerId(customerId)
                .map(data -> toDomain(data, objectMapper));
    }

    /**
     * Busca una póliza por su identificador único.
     *
     * @param id el identificador de la póliza
     * @return un {@link Mono} que emite la póliza encontrada, o vacío si no existe
     */
    @Override
    public Mono<Policy> findById(Long id) {
        return repository.findById(id)
                .map(data -> toDomain(data, objectMapper));
    }

    /**
     * Verifica si un cliente ya tiene una póliza de un tipo específico.
     * <p>
     * Útil para validar reglas de negocio como "solo una póliza de VIDA por cliente".
     * </p>
     *
     * @param customerId el identificador del cliente
     * @param type el tipo de póliza a verificar (ej: "VIDA", "VEHICULO", "SALUD")
     * @return un {@link Mono} que emite {@code true} si el cliente ya tiene una póliza de ese tipo,
     *         {@code false} en caso contrario
     */
    @Override
    public Mono<Boolean> existsByCustomerIdAndType(Long customerId, String type) {
        return repository.existsByCustomerIdAndType(customerId, type);
    }
}