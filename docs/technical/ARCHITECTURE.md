# 🏗️ Documentación de Arquitectura

## Arquitectura Hexagonal (Puertos y Adaptadores)

El proyecto implementa el patrón de **Arquitectura Hexagonal** para separar la lógica de negocio de la infraestructura.

### Capas del Sistema

```
┌─────────────────────────────────────────────────────────┐
│                    CAPA EXTERNA (HTTP)                  │
│                  (Controladores REST)                   │
└─────────────────────────────────────────────────────────┘
                           ↓
┌─────────────────────────────────────────────────────────┐
│            PUERTOS DE ENTRADA (Interfaces)              │
│      (CustomerServicePort, PolicyService)               │
└─────────────────────────────────────────────────────────┘
                           ↓
┌─────────────────────────────────────────────────────────┐
│              CAPA DE DOMINIO (Business Logic)           │
│          (Servicios, Entidades, Excepciones)           │
└─────────────────────────────────────────────────────────┘
                           ↓
┌─────────────────────────────────────────────────────────┐
│             PUERTOS DE SALIDA (Interfaces)              │
│    (CustomerRepositoryPort, PolicyRepositoryPort)       │
└─────────────────────────────────────────────────────────┘
                           ↓
┌─────────────────────────────────────────────────────────┐
│            CAPA EXTERNA (Infraestructura)               │
│           (Adaptadores, Persistencia, BD)               │
└─────────────────────────────────────────────────────────┘
```

## Estructura de Paquetes

### Customer Service

```
com.insurance.customer/
├── application/
│   └── service/
│       └── CustomerService.java          # Lógica de negocio
├── domain/
│   ├── entity/
│   │   └── Customer.java                 # Entidad de dominio
│   ├── exception/
│   │   ├── BusinessException.java
│   │   └── ErrorMessage.java
│   └── port/
│       ├── in/
│       │   └── service/
│       │       └── CustomerServicePort.java  # Puerto de entrada
│       └── out/
│           └── persistence/
│               └── CustomerRepositoryPort.java # Puerto de salida
└── infrastructure/
    └── adapter/
        ├── in/
        │   └── api/
        │       └── CustomerController.java   # Adaptador de entrada
        └── out/
            └── persistence/
                ├── CustomerPersistenceAdapter.java  # Adaptador salida
                ├── mapper/
                │   └── CustomerPersistenceMapper.java
                ├── data/
                │   └── CustomerEntity.java
                └── repository/
                    └── CustomerR2dbcRepository.java # R2DBC
```

### Policy Service

```
com.insurance.policy/
├── application/
│   └── service/
│       └── PolicyService.java            # Lógica de negocio
├── domain/
│   ├── entity/
│   │   ├── Policy.java
│   │   ├── Person.java
│   │   └── PolicyType.java
│   ├── exception/
│   │   ├── BusinessException.java
│   │   └── ErrorMessage.java
│   └── port/
│       ├── in/
│       │   └── service/
│       │       └── PolicyService.java        # Puerto de entrada
│       └── out/
│           └── persistence/
│               └── PolicyRepositoryPort.java # Puerto de salida
└── infrastructure/
    └── adapter/
        ├── in/
        │   └── api/
        │       └── PolicyController.java     # Adaptador de entrada
        └── out/
            └── persistence/
                ├── PolicyPersistenceAdapter.java   # Adaptador salida
                ├── mapper/
                │   └── PolicyPersistenceMapper.java
                ├── data/
                │   └── PolicyEntity.java
                └── repository/
                    └── PolicyR2dbcRepository.java # R2DBC
```

## Flujo de Datos

### Crear Cliente

```
HTTP POST /api/customers
    ↓
CustomerController (Adaptador Entrada)
    ↓
CustomerServicePort (Puerto Entrada)
    ↓
CustomerService (Lógica Negocio)
    ↓
CustomerRepositoryPort (Puerto Salida)
    ↓
CustomerPersistenceAdapter (Adaptador Salida)
    ↓
CustomerR2dbcRepository (BD - H2)
    ↓
HTTP 201 Created
```

### Crear Póliza (con Validaciones)

```
HTTP POST /api/policies
    ↓
PolicyController (Adaptador Entrada)
    ↓
PolicyService (Lógica Negocio)
    ├── Validación por tipo (VIDA, VEHICULO, SALUD)
    ├── Regla: VIDA - Solo una por cliente
    └── Regla: VIDA - Máx 2 beneficiarios
    ↓
PolicyRepositoryPort (Puerto Salida)
    ↓
PolicyPersistenceAdapter (Almacenamiento JSON)
    ↓
PolicyR2dbcRepository (BD - H2)
    ↓
HTTP 201 Created / 400 Bad Request
```

## Principios Implementados

### 1. Separación de Concerns
- **Dominio:** Lógica de negocio pura (sin dependencias externas)
- **Aplicación:** Casos de uso y orquestación
- **Infraestructura:** Detalles técnicos (BD, HTTP, etc.)

### 2. Inversión de Dependencias
- Los servicios del dominio NO conocen los adaptadores
- Los adaptadores implementan interfaces del dominio
- Las dependencias apuntan HACIA el dominio

### 3. Independencia de Infraestructura
- Cambiar de BD: Solo modificar adaptadores
- Cambiar de HTTP a gRPC: Solo modificar controladores
- Lógica de negocio permanece intacta

### 4. Testing
- Servicios testables sin BD real
- Mocking de puertos simplificado
- Tests unitarios rápidos

## Reglas de Negocio

### Gestión de Clientes
- Cada cliente tiene un ID único
- Email es obligatorio
- Un cliente puede tener múltiples pólizas

### Gestión de Pólizas

#### Póliza VIDA
- Solo UNA póliza de vida por cliente
- Requiere beneficiarios (mínimo 1, máximo 2)
- Beneficiarios contienen: nombre, apellido, DNI

#### Póliza VEHICULO
- Múltiples pólizas permitidas por cliente
- Requiere lista de vehículos (mínimo 1)
- Vehículos identificados por placa

#### Póliza SALUD
- Múltiples pólizas permitidas por cliente
- Permite familiares adicionales:
  - Padres (0, 1, 2)
  - Hijos (0-N)
  - Cónyuge (sí/no)

## Trazabilidad

Cada operación genera un `transactionId` único que:
- Se genera al recibir la petición (WebFilter)
- Se propaga a través del flujo reactivo
- Se incluye en las respuestas
- Se registra en logs
- Permite rastrear operaciones end-to-end

```
HTTP Request
    ↓
WebFilter (genera transactionId)
    ↓
Controller (recibe transactionId)
    ↓
Service (usa transactionId)
    ↓
TraceabilityAdapter (registra operación)
    ↓
HTTP Response (incluye transactionId)
```

## Tecnologías

### Runtime
- **Java 21 LTS** - Lenguaje de programación
- **Spring Boot 3.4.3** - Framework web
- **Spring WebFlux** - Programación reactiva
- **Project Reactor** - Implementación de reactividad

### Persistencia
- **Spring Data R2DBC** - ORM reactivo
- **H2 Database** - Base de datos en memoria
- **Jackson** - Serialización JSON

### Documentación
- **Springdoc OpenAPI** - Especificación OpenAPI
- **Swagger UI** - Documentación interactiva

### Testing
- **JUnit 5** - Framework de testing
- **Mockito** - Mocking de dependencias
- **StepVerifier** - Testing de flujos reactivos
- **WebTestClient** - Testing de endpoints

### DevOps
- **Docker** - Containerización
- **Docker Compose** - Orquestación local
- **Gradle** - Build y dependency management

## Patrón de Respuesta Unificada

Todas las respuestas siguen el formato:

```json
{
  "transactionId": "uuid-único",
  "message": "success",
  "code": 200,
  "data": { /* datos de la operación */ }
}
```

En caso de error:

```json
{
  "transactionId": "uuid-único",
  "message": "error-description",
  "code": 400,
  "data": null
}
```

## Próximas Evoluciones

### Corto Plazo
- Tests de integración
- Métricas con Actuator
- Validaciones avanzadas

### Mediano Plazo
- Caché con Redis
- Mensajería con RabbitMQ o AWS SQS
- Configuración centralizada

### Largo Plazo
- Despliegue en AWS
- Monitoreo con Prometheus/Grafana

