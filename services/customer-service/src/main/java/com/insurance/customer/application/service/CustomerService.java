package com.insurance.customer.application.service;

import com.insurance.customer.domain.entity.Customer;
import com.insurance.customer.domain.exception.BusinessException;
import com.insurance.customer.domain.exception.ErrorMessage;
import com.insurance.customer.domain.port.in.service.CustomerServicePort;
import com.insurance.customer.domain.port.out.persistence.CustomerRepositoryPort;
import com.insurance.customer.domain.port.out.traceability.TraceabilityPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * Servicio que implementa la lógica de negocio para la gestión de Clientes.
 * <p>
 * Esta clase sigue el patrón de Arquitectura Hexagonal, implementando el puerto de entrada
 * {@link CustomerServicePort} y delegando la persistencia a través del puerto de salida
 * {@link CustomerRepositoryPort}. Incluye trazabilidad completa de todas las operaciones.
 * </p>
 *
 * @author Anthony Colmenares
 * @version 1.0.0
 * @since 2026-03-02
 * @see CustomerServicePort
 * @see CustomerRepositoryPort
 * @see TraceabilityPort
 */
@Service
@RequiredArgsConstructor
public class CustomerService implements CustomerServicePort {

    /** Repositorio para operaciones de persistencia de clientes. */
    private final CustomerRepositoryPort repository;

    /** Puerto de trazabilidad para logging y auditoría. */
    private final TraceabilityPort traceability;

    /** Nombre del parámetro ID de cliente para trazabilidad. */
    private static final String CUSTOMER_ID = "customerId";

    /** Nombre de operación para creación de cliente. */
    private static final String OPERATION_CREATE = "createCustomer";

    /** Nombre de operación para búsqueda por ID. */
    private static final String OPERATION_FIND_BY_ID = "findCustomerById";

    /** Nombre de operación para listar todos los clientes. */
    private static final String OPERATION_FIND_ALL = "findAllCustomers";

    /** Nombre de operación para actualización de cliente. */
    private static final String OPERATION_UPDATE = "updateCustomer";

    /** Nombre de operación para eliminación de cliente. */
    private static final String OPERATION_DELETE = "deleteCustomer";

    /**
     * Crea un nuevo cliente en el sistema.
     * <p>
     * Este método registra la entrada de datos, persiste el cliente y registra la salida.
     * En caso de error, se registra automáticamente en el sistema de trazabilidad.
     * </p>
     *
     * @param customer el cliente a crear, no debe ser {@code null}
     * @param transactionId identificador único de transacción para trazabilidad
     * @return un {@link Mono} que emite el cliente creado con su ID asignado
     * @throws BusinessException si ocurre un error de validación o de negocio
     */
    @Override
    public Mono<Customer> create(Customer customer, String transactionId) {

        return traceability.traceIn(customer, OPERATION_CREATE, transactionId)
                .flatMap(repository::save)
                .flatMap(saved -> traceability.traceOut(saved, OPERATION_CREATE, transactionId))
                .doOnError(throwable -> traceability.traceError(throwable, OPERATION_CREATE, transactionId));
    }

    /**
     * Busca un cliente por su identificador único.
     * <p>
     * Valida que el cliente exista en el sistema antes de retornarlo.
     * Si no existe, lanza una {@link BusinessException}.
     * </p>
     *
     * @param id el identificador del cliente a buscar
     * @param transactionId identificador único de transacción para trazabilidad
     * @return un {@link Mono} que emite el cliente encontrado
     * @throws BusinessException si el cliente no existe (CUSTOMER_NOT_FOUND)
     */
    @Override
    public Mono<Customer> findById(Long id, String transactionId) {

        return traceability.traceIn(Map.of(CUSTOMER_ID, id), OPERATION_FIND_BY_ID, transactionId)
                .flatMap(request -> validateCustomerExists(id))
                .flatMap(customer -> traceability.traceOut(customer, OPERATION_FIND_BY_ID, transactionId))
                .doOnError(throwable -> traceability.traceError(throwable, OPERATION_FIND_BY_ID, transactionId));
    }

    /**
     * Obtiene todos los clientes registrados en el sistema.
     * <p>
     * Retorna un flujo reactivo con todos los clientes. Si no hay clientes,
     * retorna un flujo vacío (no lanza excepción).
     * </p>
     *
     * @param transactionId identificador único de transacción para trazabilidad
     * @return un {@link Flux} que emite todos los clientes del sistema
     */
    @Override
    public Flux<Customer> findAll(String transactionId) {

        return traceability.traceIn(new Object(), OPERATION_FIND_ALL, transactionId)
                .flatMapMany(unused -> repository.findAll())
                .flatMap(customer -> traceability.traceOut(customer, OPERATION_FIND_ALL, transactionId))
                .doOnError(throwable -> traceability.traceError(throwable, OPERATION_FIND_ALL, transactionId));
    }

    /**
     * Actualiza la información de un cliente existente.
     * <p>
     * Valida que el cliente exista antes de actualizarlo. El ID del cliente
     * en el path prevalece sobre el ID en el cuerpo de la petición.
     * </p>
     *
     * @param id el identificador del cliente a actualizar
     * @param customer los nuevos datos del cliente
     * @param transactionId identificador único de transacción para trazabilidad
     * @return un {@link Mono} que emite el cliente actualizado
     * @throws BusinessException si el cliente no existe (CUSTOMER_NOT_FOUND)
     */
    @Override
    public Mono<Customer> update(Long id, Customer customer, String transactionId) {

        return traceability.traceIn(customer, OPERATION_UPDATE, transactionId)
                .flatMap(unused -> validateCustomerExists(id))
                .flatMap(unused -> updateCustomer(id, customer))
                .flatMap(updated -> traceability.traceOut(updated, OPERATION_UPDATE, transactionId))
                .doOnError(throwable -> traceability.traceError(throwable, OPERATION_UPDATE, transactionId));
    }

    /**
     * Método privado auxiliar para actualizar un cliente.
     * <p>
     * Asigna el ID correcto al cliente antes de persistirlo.
     * </p>
     *
     * @param id el ID del cliente a actualizar
     * @param customer los datos del cliente
     * @return un {@link Mono} que emite el cliente actualizado
     */
    private Mono<Customer> updateCustomer(Long id, Customer customer) {
        customer.setId(id);
        return repository.save(customer);
    }

    /**
     * Elimina un cliente del sistema.
     * <p>
     * Valida que el cliente exista antes de eliminarlo. Esta operación
     * es irreversible.
     * </p>
     *
     * @param id el identificador del cliente a eliminar
     * @param transactionId identificador único de transacción para trazabilidad
     * @return un {@link Mono} vacío que completa cuando la eliminación es exitosa
     * @throws BusinessException si el cliente no existe (CUSTOMER_NOT_FOUND)
     */
    @Override
    public Mono<Void> delete(Long id, String transactionId) {

        return traceability.traceIn(Map.of(CUSTOMER_ID, id), OPERATION_DELETE, transactionId)
                .flatMap(unused -> validateCustomerExists(id))
                .flatMap(unused -> repository.deleteById(id).thenReturn(id))
                .flatMap(unused -> traceability.traceOut(Map.of(CUSTOMER_ID, id), OPERATION_DELETE, transactionId))
                .doOnError(throwable -> traceability.traceError(throwable, OPERATION_DELETE, transactionId))
                .then();
    }

    /**
     * Valida que un cliente exista en el sistema.
     * <p>
     * Método privado auxiliar para validaciones comunes.
     * </p>
     *
     * @param id el identificador del cliente a validar
     * @return un {@link Mono} que emite el cliente si existe
     * @throws BusinessException si el cliente no existe (CUSTOMER_NOT_FOUND)
     */
    private Mono<Customer> validateCustomerExists(Long id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new BusinessException(ErrorMessage.CUSTOMER_NOT_FOUND)));
    }
}
