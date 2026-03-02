package com.insurance.customer.infrastructure.adapter.out.persistence;

import com.insurance.customer.domain.entity.Customer;
import com.insurance.customer.domain.port.out.persistence.CustomerRepositoryPort;
import com.insurance.customer.infrastructure.adapter.out.persistence.mapper.CustomerPersistenceMapper;
import com.insurance.customer.infrastructure.adapter.out.persistence.repository.CustomerR2dbcRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Adaptador de salida para la persistencia de clientes usando R2DBC y H2.
 * <p>
 * Esta clase implementa el patrón de Arquitectura Hexagonal como adaptador de salida,
 * conectando la lógica de negocio del dominio con la infraestructura de persistencia.
 * Utiliza Spring Data R2DBC para operaciones reactivas sobre la base de datos H2.
 * </p>
 * <p>
 * <b>Responsabilidades:</b>
 * </p>
 * <ul>
 *   <li>Convertir entidades de dominio ({@link Customer}) a entidades de persistencia ({@code CustomerEntity})</li>
 *   <li>Convertir entidades de persistencia de vuelta a entidades de dominio</li>
 *   <li>Delegar operaciones CRUD al repositorio R2DBC</li>
 *   <li>Mantener la separación entre dominio e infraestructura</li>
 * </ul>
 *
 * @author Anthony Colmenares
 * @version 1.0.0
 * @since 2026-03-02
 * @see CustomerRepositoryPort
 * @see CustomerPersistenceMapper
 * @see CustomerR2dbcRepository
 */
@Component
@RequiredArgsConstructor
public class CustomerPersistencePersistenceAdapter implements CustomerRepositoryPort, CustomerPersistenceMapper {

    /** Repositorio R2DBC para operaciones de base de datos reactivas. */
    private final CustomerR2dbcRepository repository;

    /**
     * Guarda o actualiza un cliente en la base de datos.
     * <p>
     * Si el cliente tiene un {@code id}, se actualiza el registro existente.
     * Si no tiene {@code id}, se crea un nuevo registro y se asigna un ID automáticamente.
     * </p>
     *
     * @param customer el cliente a guardar o actualizar
     * @return un {@link Mono} que emite el cliente guardado con su ID asignado
     */
    @Override
    public Mono<Customer> save(Customer customer) {
        return repository.save(toData(customer))
                .map(this::toDomain);
    }

    /**
     * Busca un cliente por su identificador único.
     *
     * @param id el identificador del cliente
     * @return un {@link Mono} que emite el cliente encontrado, o vacío si no existe
     */
    @Override
    public Mono<Customer> findById(Long id) {
        return repository.findById(id)
                .map(this::toDomain);
    }

    /**
     * Obtiene todos los clientes registrados en la base de datos.
     *
     * @return un {@link Flux} que emite todos los clientes, o vacío si no hay ninguno
     */
    @Override
    public Flux<Customer> findAll() {
        return repository.findAll()
                .map(this::toDomain);
    }

    /**
     * Elimina un cliente de la base de datos por su identificador.
     * <p>
     * Esta operación es irreversible. No lanza error si el ID no existe.
     * </p>
     *
     * @param id el identificador del cliente a eliminar
     * @return un {@link Mono} vacío que completa cuando la eliminación finaliza
     */
    @Override
    public Mono<Void> deleteById(Long id) {
        return repository.deleteById(id);
    }
}