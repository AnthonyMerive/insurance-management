package com.insurance.policy.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Entidad de dominio que representa una Póliza de Seguro.
 * <p>
 * Esta clase es el modelo central del dominio de pólizas, soportando tres tipos diferentes
 * de seguros: VIDA, VEHICULO y SALUD. Cada tipo tiene atributos específicos que se utilizan
 * según corresponda.
 * </p>
 * <p>
 * <b>Tipos de Póliza y sus Atributos:</b>
 * </p>
 * <ul>
 *   <li><b>VIDA:</b> Utiliza {@code beneficiaries} (obligatorio, máx. 2 personas)</li>
 *   <li><b>VEHICULO:</b> Utiliza {@code insuredVehicles} (obligatorio, lista de placas)</li>
 *   <li><b>SALUD:</b> Utiliza {@code extraParents}, {@code extraChildren}, {@code hasSpouse}</li>
 * </ul>
 * <p>
 * Los atributos no aplicables a un tipo de póliza deben ser {@code null}.
 * </p>
 *
 * @author Anthony Colmenares
 * @version 1.0.0
 * @since 2026-03-02
 * @see PolicyType
 * @see Person
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Policy {

    /**
     * Identificador único de la póliza.
     * <p>
     * Generado automáticamente por la base de datos al crear una nueva póliza.
     * Es {@code null} antes de la persistencia.
     * </p>
     */
    private Long id;

    /**
     * Identificador del cliente dueño de esta póliza.
     * <p>
     * Referencia al cliente en el sistema de gestión de clientes.
     * Campo obligatorio al crear una póliza.
     * </p>
     */
    private Long customerId;

    /**
     * Tipo de póliza de seguro.
     * <p>
     * Determina qué atributos de la póliza son aplicables y qué reglas
     * de negocio se deben aplicar.
     * </p>
     * <p>
     * Valores posibles: {@link PolicyType#VIDA}, {@link PolicyType#VEHICULO}, {@link PolicyType#SALUD}
     * </p>
     */
    private PolicyType type;

    /**
     * Lista de beneficiarios de la póliza.
     * <p>
     * <b>Aplicable a:</b> Pólizas de tipo VIDA
     * </p>
     * <p>
     * <b>Reglas:</b>
     * </p>
     * <ul>
     *   <li>Obligatorio para pólizas VIDA</li>
     *   <li>Máximo 2 beneficiarios permitidos</li>
     *   <li>Debe ser {@code null} para otros tipos de póliza</li>
     * </ul>
     *
     * @see Person
     */
    private List<Person> beneficiaries;

    /**
     * Lista de vehículos asegurados bajo esta póliza.
     * <p>
     * <b>Aplicable a:</b> Pólizas de tipo VEHICULO
     * </p>
     * <p>
     * <b>Formato:</b> Lista de placas de vehículos (ej: ["ABC123", "XYZ789"])
     * </p>
     * <p>
     * <b>Reglas:</b>
     * </p>
     * <ul>
     *   <li>Obligatorio para pólizas VEHICULO (mínimo 1 vehículo)</li>
     *   <li>Debe ser {@code null} para otros tipos de póliza</li>
     * </ul>
     */
    private List<String> insuredVehicles;

    /**
     * Número de padres adicionales cubiertos por la póliza.
     * <p>
     * <b>Aplicable a:</b> Pólizas de tipo SALUD
     * </p>
     * <p>
     * Permite incluir a los padres del asegurado en la cobertura.
     * Valor típico: 0, 1 o 2 (ambos padres).
     * </p>
     * <p>
     * Debe ser {@code null} o 0 para otros tipos de póliza.
     * </p>
     */
    private Integer extraParents;

    /**
     * Número de hijos adicionales cubiertos por la póliza.
     * <p>
     * <b>Aplicable a:</b> Pólizas de tipo SALUD
     * </p>
     * <p>
     * Permite incluir a los hijos del asegurado en la cobertura.
     * No hay límite máximo típicamente, pero puede afectar la prima.
     * </p>
     * <p>
     * Debe ser {@code null} o 0 para otros tipos de póliza.
     * </p>
     */
    private Integer extraChildren;

    /**
     * Indica si el cónyuge está cubierto por la póliza.
     * <p>
     * <b>Aplicable a:</b> Pólizas de tipo SALUD
     * </p>
     * <p>
     * {@code true} si el cónyuge está incluido en la cobertura,
     * {@code false} o {@code null} en caso contrario.
     * </p>
     * <p>
     * Debe ser {@code null} o {@code false} para otros tipos de póliza.
     * </p>
     */
    private Boolean hasSpouse;
}

