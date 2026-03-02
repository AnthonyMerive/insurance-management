package com.insurance.policy.application.service;

import com.insurance.policy.domain.entity.Person;
import com.insurance.policy.domain.entity.Policy;
import com.insurance.policy.domain.entity.PolicyType;
import com.insurance.policy.domain.exception.BusinessException;
import com.insurance.policy.domain.exception.ErrorMessage;
import com.insurance.policy.domain.port.out.persistence.PolicyRepositoryPort;
import com.insurance.policy.domain.port.out.traceability.TraceabilityPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Map;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

/**
 * Servicio que implementa la lógica de negocio para la gestión de Pólizas de Seguros.
 * <p>
 * Esta clase gestiona los tres tipos de pólizas disponibles: VIDA, VEHICULO y SALUD.
 * Implementa reglas de negocio específicas para cada tipo:
 * </p>
 * <ul>
 *   <li><b>VIDA:</b> Solo una póliza por cliente, máximo 2 beneficiarios</li>
 *   <li><b>VEHICULO:</b> Requiere al menos un vehículo asegurado</li>
 *   <li><b>SALUD:</b> Permite familiares adicionales (padres, hijos, cónyuge)</li>
 * </ul>
 *
 * @author Anthony Colmenares
 * @version 1.0.0
 * @since 2026-03-02
 * @see com.insurance.policy.domain.port.in.service.PolicyService
 * @see PolicyRepositoryPort
 * @see TraceabilityPort
 */
@Service
@RequiredArgsConstructor
public class PolicyService implements com.insurance.policy.domain.port.in.service.PolicyService {

    /** Repositorio para operaciones de persistencia de pólizas. */
    private final PolicyRepositoryPort repository;

    /** Puerto de trazabilidad para logging y auditoría. */
    private final TraceabilityPort traceability;

    /** Nombre del parámetro ID de cliente para trazabilidad. */
    private static final String CUSTOMER_ID = "customerId";

    /** Nombre del parámetro ID de póliza para trazabilidad. */
    private static final String POLICY_ID = "policyId";

    /** Nombre de operación para creación de póliza. */
    private static final String OPERATION_CREATE = "createPolicy";

    /** Nombre de operación para búsqueda por cliente. */
    private static final String OPERATION_FIND_BY_CUSTOMER = "findByCustomerId";

    /** Nombre de operación para búsqueda por ID. */
    private static final String OPERATION_FIND_BY_ID = "findById";

    /** Nombre de operación para obtener beneficiarios. */
    private static final String OPERATION_FIND_BENEFICIARIES = "findBeneficiariesByPolicyId";

    /**
     * Crea una nueva póliza de seguro aplicando las reglas de negocio según el tipo.
     * <p>
     * <b>Reglas de Negocio:</b>
     * </p>
     * <ul>
     *   <li><b>Póliza de VIDA:</b>
     *     <ul>
     *       <li>Solo se permite una póliza de vida por cliente</li>
     *       <li>Máximo 2 beneficiarios permitidos</li>
     *       <li>Los beneficiarios son obligatorios</li>
     *     </ul>
     *   </li>
     *   <li><b>Póliza de VEHICULO:</b>
     *     <ul>
     *       <li>Debe tener al menos un vehículo asegurado</li>
     *       <li>Múltiples pólizas permitidas por cliente</li>
     *     </ul>
     *   </li>
     *   <li><b>Póliza de SALUD:</b>
     *     <ul>
     *       <li>Puede incluir familiares adicionales (padres, hijos, cónyuge)</li>
     *       <li>Múltiples pólizas permitidas por cliente</li>
     *     </ul>
     *   </li>
     * </ul>
     *
     * @param policy la póliza a crear, no debe ser {@code null}
     * @param transactionId identificador único de transacción para trazabilidad
     * @return un {@link Mono} que emite la póliza creada con su ID asignado
     * @throws BusinessException si el cliente ya tiene una póliza de vida (CUSTOMER_ALREADY_HAS_LIFE_POLICY)
     * @throws BusinessException si la póliza de vida tiene más de 2 beneficiarios (LIFE_POLICY_MAX_BENEFICIARIES)
     */
    @Override
    public Mono<Policy> createPolicy(Policy policy, String transactionId) {
        return traceability.traceIn(policy, OPERATION_CREATE, transactionId)
                .flatMap(unused -> policy.getType() == PolicyType.VIDA
                        ? repository.existsByCustomerIdAndType(policy.getCustomerId(), PolicyType.VIDA.name())
                        .flatMap(exists -> policyValidations(policy, exists))
                        : repository.save(policy))
                .flatMap(saved -> traceability.traceOut(saved, OPERATION_CREATE, transactionId))
                .doOnError(throwable -> traceability.traceError(throwable, OPERATION_CREATE, transactionId));
    }

    /**
     * Valida las reglas de negocio específicas para pólizas de VIDA.
     * <p>
     * Método privado auxiliar que verifica:
     * </p>
     * <ul>
     *   <li>Que el cliente no tenga ya una póliza de vida</li>
     *   <li>Que no se excedan los 2 beneficiarios permitidos</li>
     * </ul>
     *
     * @param policy la póliza a validar
     * @param exists {@code true} si el cliente ya tiene una póliza de vida
     * @return un {@link Mono} que emite la póliza guardada si las validaciones pasan
     * @throws BusinessException si las validaciones fallan
     */
    private Mono<Policy> policyValidations(Policy policy, Boolean exists) {
        if (exists) {
            return Mono.error(new BusinessException(ErrorMessage.CUSTOMER_ALREADY_HAS_LIFE_POLICY));
        }

        if (nonNull(policy.getBeneficiaries()) && policy.getBeneficiaries().size() > 2) {
            return Mono.error(new BusinessException(ErrorMessage.LIFE_POLICY_MAX_BENEFICIARIES));
        }
        return repository.save(policy);
    }

    /**
     * Busca todas las pólizas asociadas a un cliente específico.
     * <p>
     * Retorna todas las pólizas (de cualquier tipo) que pertenezcan al cliente.
     * Si el cliente no tiene pólizas, se lanza una excepción.
     * </p>
     *
     * @param customerId el identificador del cliente
     * @param transactionId identificador único de transacción para trazabilidad
     * @return un {@link Flux} que emite todas las pólizas del cliente
     * @throws BusinessException si el cliente no tiene pólizas (POLICY_NOT_FOUND)
     */
    @Override
    public Flux<Policy> findByCustomerId(Long customerId, String transactionId) {
        return traceability.traceIn(Map.of(CUSTOMER_ID, customerId), OPERATION_FIND_BY_CUSTOMER, transactionId)
                .flatMapMany(req -> repository.findByCustomerId(customerId))
                .switchIfEmpty(Mono.error(new BusinessException(ErrorMessage.POLICY_NOT_FOUND)))
                .flatMap(policy -> traceability.traceOut(policy, OPERATION_FIND_BY_CUSTOMER, transactionId))
                .doOnError(throwable -> traceability.traceError(throwable, OPERATION_FIND_BY_CUSTOMER, transactionId));
    }

    /**
     * Busca una póliza por su identificador único.
     * <p>
     * Retorna los detalles completos de la póliza incluyendo todos sus atributos
     * específicos según el tipo (beneficiarios, vehículos, familiares, etc.).
     * </p>
     *
     * @param id el identificador de la póliza a buscar
     * @param transactionId identificador único de transacción para trazabilidad
     * @return un {@link Mono} que emite la póliza encontrada
     * @throws BusinessException si la póliza no existe (POLICY_NOT_FOUND)
     */
    @Override
    public Mono<Policy> findById(Long id, String transactionId) {
        return traceability.traceIn(Map.of(POLICY_ID, id), OPERATION_FIND_BY_ID, transactionId)
                .flatMap(req -> repository.findById(id))
                .switchIfEmpty(Mono.error(new BusinessException(ErrorMessage.POLICY_NOT_FOUND)))
                .flatMap(policy -> traceability.traceOut(policy, OPERATION_FIND_BY_ID, transactionId))
                .doOnError(throwable -> traceability.traceError(throwable, OPERATION_FIND_BY_ID, transactionId));
    }

    /**
     * Obtiene la lista de beneficiarios de una póliza específica.
     * <p>
     * Este método es aplicable principalmente a pólizas de tipo VIDA.
     * Si la póliza no tiene beneficiarios o no es de tipo VIDA, retorna una lista vacía.
     * </p>
     *
     * @param id el identificador de la póliza
     * @param transactionId identificador único de transacción para trazabilidad
     * @return un {@link Flux} que emite todos los beneficiarios de la póliza
     * @throws BusinessException si la póliza no existe (POLICY_NOT_FOUND)
     */
    @Override
    public Flux<Person> findBeneficiariesByPolicyId(Long id, String transactionId) {
        return traceability.traceIn(Map.of(POLICY_ID, id), OPERATION_FIND_BENEFICIARIES, transactionId)
                .flatMapMany(req -> repository.findById(id))
                .switchIfEmpty(Mono.error(new BusinessException(ErrorMessage.POLICY_NOT_FOUND)))
                .flatMapIterable(policy -> isNull(policy.getBeneficiaries())
                        ? Collections.emptyList()
                        : policy.getBeneficiaries()
                )
                .flatMap(person -> traceability.traceOut(person, OPERATION_FIND_BENEFICIARIES, transactionId))
                .doOnError(throwable -> traceability.traceError(throwable, OPERATION_FIND_BENEFICIARIES, transactionId));
    }
}
