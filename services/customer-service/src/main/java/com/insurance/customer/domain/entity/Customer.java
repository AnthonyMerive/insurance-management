package com.insurance.customer.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Entidad de dominio que representa un Cliente en el sistema de seguros.
 * <p>
 * Esta clase es el modelo central del dominio de clientes, conteniendo toda la información
 * personal necesaria para la gestión de seguros. Sigue los principios de DDD (Domain-Driven Design)
 * y es independiente de la infraestructura de persistencia.
 * </p>
 * <p>
 * Los clientes son la entidad principal que puede tener múltiples pólizas de seguro asociadas.
 * </p>
 *
 * @author Anthony Colmenares
 * @version 1.0.0
 * @since 2026-03-02
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    /**
     * Identificador único del cliente.
     * <p>
     * Generado automáticamente por la base de datos al crear un nuevo cliente.
     * Es {@code null} antes de la persistencia.
     * </p>
     */
    private Long id;

    /**
     * Tipo de documento de identificación del cliente.
     * <p>
     * Ejemplos: "CC" (Cédula de Ciudadanía), "CE" (Cédula de Extranjería),
     * "Pasaporte", "NIT", etc.
     * </p>
     */
    private String documentType;

    /**
     * Número del documento de identificación del cliente.
     * <p>
     * Debe ser único en el sistema para cada tipo de documento.
     * </p>
     */
    private String documentNumber;

    /**
     * Nombre(s) del cliente.
     * <p>
     * Campo obligatorio para la creación de un cliente.
     * </p>
     */
    private String firstName;

    /**
     * Apellido(s) del cliente.
     * <p>
     * Campo obligatorio para la creación de un cliente.
     * </p>
     */
    private String lastName;

    /**
     * Correo electrónico del cliente.
     * <p>
     * Campo obligatorio y debe tener un formato válido de email.
     * Utilizado para comunicaciones y notificaciones.
     * </p>
     */
    private String email;

    /**
     * Número de teléfono del cliente.
     * <p>
     * Puede incluir código de país. Ejemplos: "+57-300-000-0000", "3001234567"
     * </p>
     */
    private String phone;

    /**
     * Fecha de nacimiento del cliente.
     * <p>
     * Utilizada para cálculos de edad, validaciones de elegibilidad para ciertos
     * tipos de pólizas, y determinación de tarifas.
     * </p>
     * <p>
     * Formato esperado: ISO-8601 (yyyy-MM-dd)
     * </p>
     */
    private LocalDate birthDate;
}
