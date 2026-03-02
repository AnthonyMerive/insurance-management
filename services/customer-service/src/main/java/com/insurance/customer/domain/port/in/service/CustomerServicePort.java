package com.insurance.customer.domain.port.in.service;

import com.insurance.customer.domain.entity.Customer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Puerto de entrada para el caso de uso de gestión de clientes.
 * <p>
 * Esta interfaz define el contrato para las operaciones de negocio relacionadas con clientes
 * en el patrón de Arquitectura Hexagonal. Representa los casos de uso que el sistema expone
 * a los adaptadores de entrada (como controladores REST).
 * </p>
 * <p>
 * <b>Principios:</b>
 * </p>
 * <ul>
 *   <li>Define el "qué" pero no el "cómo" (responsabilidad de la implementación)</li>
 *   <li>Independiente de la infraestructura y tecnologías específicas</li>
 *   <li>Todas las operaciones son reactivas (retornan Mono o Flux)</li>
 *   <li>Incluye transactionId para trazabilidad de operaciones</li>
 * </ul>
 *
 * @author Anthony Colmenares
 * @version 1.0.0
 * @since 2026-03-02
 * @see Customer
 */
public interface CustomerServicePort {

    /**
     * Crea un nuevo cliente en el sistema.
     *
     * @param customer el cliente a crear, no debe ser {@code null}
     * @param transactionId identificador único de transacción para trazabilidad
     * @return un {@link Mono} que emite el cliente creado con su ID asignado
     */
    Mono<Customer> create(Customer customer, String transactionId);

    /**
     * Busca un cliente por su identificador único.
     *
     * @param id el identificador del cliente
     * @param transactionId identificador único de transacción para trazabilidad
     * @return un {@link Mono} que emite el cliente encontrado
     */
    Mono<Customer> findById(Long id, String transactionId);

    /**
     * Obtiene todos los clientes registrados en el sistema.
     *
     * @param transactionId identificador único de transacción para trazabilidad
     * @return un {@link Flux} que emite todos los clientes
     */
    Flux<Customer> findAll(String transactionId);

    /**
     * Actualiza la información de un cliente existente.
     *
     * @param id el identificador del cliente a actualizar
     * @param customer los nuevos datos del cliente
     * @param transactionId identificador único de transacción para trazabilidad
     * @return un {@link Mono} que emite el cliente actualizado
     */
    Mono<Customer> update(Long id, Customer customer, String transactionId);

    /**
     * Elimina un cliente del sistema.
     *
     * @param id el identificador del cliente a eliminar
     * @param transactionId identificador único de transacción para trazabilidad
     * @return un {@link Mono} vacío que completa cuando la eliminación es exitosa
     */
    Mono<Void> delete(Long id, String transactionId);
}
