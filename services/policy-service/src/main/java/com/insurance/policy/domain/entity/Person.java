package com.insurance.policy.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad de dominio que representa una Persona (Beneficiario).
 * <p>
 * Esta clase se utiliza principalmente para representar beneficiarios en pólizas de tipo VIDA.
 * Contiene la información básica de identificación de una persona que puede recibir
 * los beneficios de una póliza de seguro.
 * </p>
 * <p>
 * <b>Uso principal:</b> Pólizas de VIDA (lista de beneficiarios)
 * </p>
 *
 * @author Anthony Colmenares
 * @version 1.0.0
 * @since 2026-03-02
 * @see Policy
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Person {

    /**
     * Nombre(s) de la persona beneficiaria.
     * <p>
     * Campo obligatorio para identificar al beneficiario.
     * </p>
     */
    private String firstName;

    /**
     * Apellido(s) de la persona beneficiaria.
     * <p>
     * Campo obligatorio para identificar al beneficiario.
     * </p>
     */
    private String lastName;

    /**
     * Número de documento de identificación de la persona.
     * <p>
     * También conocido como DNI (Documento Nacional de Identidad).
     * Debe ser un identificador único que permita verificar la identidad
     * del beneficiario al momento de reclamar los beneficios.
     * </p>
     * <p>
     * Ejemplos: "12345678", "1234567890"
     * </p>
     */
    private String documentNumber;
}

