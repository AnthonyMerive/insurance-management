package com.insurance.policy.infrastructure.adapter.out.persistence.repository;

import com.insurance.policy.infrastructure.adapter.out.persistence.data.PolicyEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Repositorio reactivo R2DBC para operaciones CRUD sobre la entidad {@link PolicyEntity}.
 * <p>
 * Esta interfaz extiende {@link ReactiveCrudRepository} de Spring Data R2DBC y define
 * métodos de consulta personalizados para el dominio de pólizas de seguros.
 * </p>
 * <p>
 * <b>Operaciones Personalizadas:</b>
 * </p>
 * <ul>
 *   <li>{@link #findByCustomerId(Long)} - Busca pólizas por cliente</li>
 *   <li>{@link #existsByCustomerIdAndType(Long, String)} - Verifica existencia de póliza por tipo</li>
 *   <li>{@link #getPolicyDetailsStoredProc(Long)} - Ejecuta procedimiento almacenado (ejemplo)</li>
 * </ul>
 * <p>
 * <b>Tecnología:</b> Spring Data R2DBC con base de datos H2 en memoria
 * </p>
 *
 * @author Anthony Colmenares
 * @version 1.0.0
 * @since 2026-03-02
 * @see PolicyEntity
 * @see ReactiveCrudRepository
 */
public interface PolicyR2dbcRepository extends ReactiveCrudRepository<PolicyEntity, Long> {

    /**
     * Busca todas las pólizas asociadas a un cliente específico.
     * <p>
     * Método derivado de Spring Data R2DBC basado en la convención de nombres.
     * Se traduce automáticamente a: {@code SELECT * FROM policies WHERE customer_id = ?}
     * </p>
     *
     * @param customerId el identificador del cliente
     * @return un {@link Flux} que emite todas las pólizas del cliente
     */
    Flux<PolicyEntity> findByCustomerId(Long customerId);

    /**
     * Verifica si existe una póliza para un cliente específico de un tipo determinado.
     * <p>
     * Útil para validar reglas de negocio como "solo una póliza de VIDA por cliente".
     * Método derivado de Spring Data R2DBC basado en la convención de nombres.
     * </p>
     *
     * @param customerId el identificador del cliente
     * @param type el tipo de póliza (ej: "VIDA", "VEHICULO", "SALUD")
     * @return un {@link Mono} que emite {@code true} si existe, {@code false} en caso contrario
     */
    Mono<Boolean> existsByCustomerIdAndType(Long customerId, String type);

    /**
     * Obtiene los detalles de una póliza mediante un procedimiento almacenado.
     * <p>
     * Este es un método de ejemplo que demuestra cómo llamar a procedimientos almacenados
     * en H2 usando la anotación {@link Query}.
     * </p>
     * <p>
     * <b>Nota:</b> El procedimiento almacenado {@code GET_POLICY_DETAILS} debe existir en la base de datos.
     * </p>
     *
     * @param id el identificador de la póliza
     * @return un {@link Mono} que emite la entidad de póliza con sus detalles
     */
    @Query("CALL GET_POLICY_DETAILS(:id)")
    Mono<PolicyEntity> getPolicyDetailsStoredProc(Long id);
}
