package com.insurance.customer.domain.port.out.persistence;

import com.insurance.customer.domain.entity.Customer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Puerto de salida para la persistencia de clientes.
 * <p>
 * Esta interfaz define el contrato para las operaciones de persistencia en el patrón
 * de Arquitectura Hexagonal. Representa las operaciones que el dominio necesita de la
 * infraestructura de persistencia, sin depender de ninguna tecnología específica.
 * </p>
 * <p>
 * <b>Principios:</b>
 * </p>
 * <ul>
 *   <li>Define operaciones CRUD básicas sobre clientes</li>
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
 * @see Customer
 */
public interface CustomerRepositoryPort {

    /**
     * Guarda o actualiza un cliente en el sistema de persistencia.
     *
     * @param customer el cliente a guardar
     * @return un {@link Mono} que emite el cliente guardado con su ID asignado
     */
    Mono<Customer> save(Customer customer);

    /**
     * Busca un cliente por su identificador único.
     *
     * @param id el identificador del cliente
     * @return un {@link Mono} que emite el cliente encontrado, o vacío si no existe
     */
    Mono<Customer> findById(Long id);

    /**
     * Obtiene todos los clientes del sistema de persistencia.
     *
     * @return un {@link Flux} que emite todos los clientes, o vacío si no hay ninguno
     */
    Flux<Customer> findAll();

    /**
     * Elimina un cliente del sistema de persistencia.
     *
     * @param id el identificador del cliente a eliminar
     * @return un {@link Mono} vacío que completa cuando la eliminación finaliza
     */
    Mono<Void> deleteById(Long id);
}
