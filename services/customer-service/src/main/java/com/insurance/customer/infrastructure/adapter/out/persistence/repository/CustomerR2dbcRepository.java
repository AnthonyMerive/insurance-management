package com.insurance.customer.infrastructure.adapter.out.persistence.repository;

import com.insurance.customer.infrastructure.adapter.out.persistence.data.CustomerEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

/**
 * Repositorio reactivo R2DBC para operaciones CRUD sobre la entidad {@link CustomerEntity}.
 * <p>
 * Esta interfaz extiende {@link ReactiveCrudRepository} de Spring Data R2DBC,
 * proporcionando operaciones básicas de base de datos de forma reactiva (no bloqueante).
 * </p>
 * <p>
 * <b>Operaciones Disponibles (heredadas):</b>
 * </p>
 * <ul>
 *   <li>{@code save(CustomerEntity)} - Guarda o actualiza un cliente</li>
 *   <li>{@code findById(Long)} - Busca un cliente por ID</li>
 *   <li>{@code findAll()} - Obtiene todos los clientes</li>
 *   <li>{@code deleteById(Long)} - Elimina un cliente por ID</li>
 *   <li>{@code count()} - Cuenta el total de clientes</li>
 *   <li>Y más operaciones CRUD estándar...</li>
 * </ul>
 * <p>
 * <b>Tecnología:</b> Spring Data R2DBC con base de datos H2 en memoria
 * </p>
 *
 * @author Anthony Colmenares
 * @version 1.0.0
 * @since 2026-03-02
 * @see CustomerEntity
 * @see ReactiveCrudRepository
 */
public interface CustomerR2dbcRepository extends ReactiveCrudRepository<CustomerEntity, Long> {
}
