package com.insurance.policy.domain.entity;

/**
 * Enumeración de tipos de pólizas de seguro disponibles en el sistema.
 * <p>
 * Define los tres tipos de seguros soportados, cada uno con reglas de negocio
 * y atributos específicos.
 * </p>
 * <p>
 * <b>Reglas de Negocio por Tipo:</b>
 * </p>
 * <ul>
 *   <li><b>VIDA:</b>
 *     <ul>
 *       <li>Solo una póliza permitida por cliente</li>
 *       <li>Requiere beneficiarios (máximo 2)</li>
 *       <li>Atributos aplicables: {@code beneficiaries}</li>
 *     </ul>
 *   </li>
 *   <li><b>VEHICULO:</b>
 *     <ul>
 *       <li>Múltiples pólizas permitidas por cliente</li>
 *       <li>Requiere al menos un vehículo asegurado</li>
 *       <li>Atributos aplicables: {@code insuredVehicles}</li>
 *     </ul>
 *   </li>
 *   <li><b>SALUD:</b>
 *     <ul>
 *       <li>Múltiples pólizas permitidas por cliente</li>
 *       <li>Puede incluir familiares adicionales</li>
 *       <li>Atributos aplicables: {@code extraParents}, {@code extraChildren}, {@code hasSpouse}</li>
 *     </ul>
 *   </li>
 * </ul>
 *
 * @author Anthony Colmenares
 * @version 1.0.0
 * @since 2026-03-02
 * @see Policy
 */
public enum PolicyType {

    /**
     * Póliza de Seguro de Vida.
     * <p>
     * Proporciona cobertura en caso de fallecimiento del asegurado.
     * Los beneficios se pagan a los beneficiarios designados.
     * </p>
     * <p>
     * <b>Restricción:</b> Solo una póliza de vida por cliente.
     * </p>
     */
    VIDA,

    /**
     * Póliza de Seguro de Vehículo.
     * <p>
     * Proporciona cobertura para daños, robo o pérdida total de vehículos.
     * Puede incluir responsabilidad civil.
     * </p>
     * <p>
     * <b>Requisito:</b> Al menos un vehículo debe estar asegurado.
     * </p>
     */
    VEHICULO,

    /**
     * Póliza de Seguro de Salud.
     * <p>
     * Proporciona cobertura médica para el asegurado y opcionalmente
     * para sus familiares (cónyuge, hijos, padres).
     * </p>
     * <p>
     * <b>Flexibilidad:</b> Permite personalización de la cobertura familiar.
     * </p>
     */
    SALUD
}

