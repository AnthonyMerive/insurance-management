# 🚀 Guía de Despliegue y Pruebas

## 📋 Prerequisitos

- **Java 21 LTS** instalado
- **Docker** y **Docker Compose** instalados
- **Postman** (opcional, para pruebas de API)

## 🐳 Despliegue con Docker

### 1. Construir y Levantar Servicios

```bash
# Desde la raíz del proyecto
docker-compose up --build
```

Este comando:
- Construye las imágenes Docker para ambos servicios
- Levanta los contenedores
- Expone los puertos 8081 (customer-service) y 8082 (policy-service)

### 2. Verificar que los Servicios Estén Corriendo

```bash
# Customer Service
curl http://localhost:8081/api/customers

# Policy Service  
curl http://localhost:8082/api/policies
```

### 3. Detener los Servicios

```bash
docker-compose down
```

## 🔧 Despliegue Local (sin Docker)

### 1. Compilar el Proyecto

```bash
./gradlew clean build
```

### 2. Ejecutar Customer Service

```bash
./gradlew :services:customer-service:bootRun
```

### 3. Ejecutar Policy Service (en otra terminal)

```bash
./gradlew :services:policy-service:bootRun
```

## 📖 Documentación de APIs

### Swagger UI

Una vez que los servicios estén corriendo, accede a:

- **Customer Service**: http://localhost:8081/swagger-ui.html
- **Policy Service**: http://localhost:8082/swagger-ui.html

### Consolas H2 (Bases de Datos)

- **Customer DB**: http://localhost:8091
  - JDBC URL: `jdbc:h2:mem:customerdb`
  - Usuario: `sa`
  - Password: `password`

- **Policy DB**: http://localhost:8092
  - JDBC URL: `jdbc:h2:mem:policydb`
  - Usuario: `sa`
  - Password: `password`

## 📮 Importar Colección de Postman

### Paso 1: Abrir Postman

1. Abre Postman Desktop o Postman Web
2. Haz clic en **Import** (esquina superior izquierda)

### Paso 2: Importar el Archivo

1. Selecciona **File**
2. Navega hasta `docs/api/insurance-management.postman_collection.json`
3. Haz clic en **Import**

### Paso 3: Configurar Variables (Opcional)

Las variables ya están configuradas por defecto:
- `customerBaseUrl`: http://localhost:8081
- `policyBaseUrl`: http://localhost:8082
- `customerId`: 1

Puedes modificarlas en la sección **Variables** de la colección.

## 🧪 Pruebas Básicas

### 1. Crear un Cliente

```bash
curl -X POST http://localhost:8081/api/customers \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Ana",
    "lastName": "Perez",
    "email": "ana.perez@example.com",
    "phoneNumber": "+57-300-000-0000",
    "birthDate": "1990-01-15"
  }'
```

**Respuesta esperada:**
```json
{
  "transactionId": "uuid-generado",
  "message": "success",
  "code": 201,
  "data": {
    "id": 1,
    "firstName": "Ana",
    "lastName": "Perez",
    "email": "ana.perez@example.com",
    "phoneNumber": "+57-300-000-0000",
    "birthDate": "1990-01-15"
  }
}
```

### 2. Listar Clientes

```bash
curl http://localhost:8081/api/customers
```

### 3. Crear una Póliza

```bash
curl -X POST http://localhost:8082/api/policies \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": 1,
    "type": "VIDA",
    "beneficiaries": [
      {
        "firstName": "Luis",
        "lastName": "Gomez",
        "dni": "12345678"
      }
    ],
    "insuredVehicles": ["ABC123"],
    "extraParents": 0,
    "extraChildren": 1,
    "hasSpouse": true
  }'
```

### 4. Obtener Pólizas de un Cliente

```bash
curl http://localhost:8082/api/policies/customer/1
```

### 5. Obtener Beneficiarios de una Póliza

```bash
curl http://localhost:8082/api/policies/1/beneficiaries
```

## 🧪 Ejecutar Pruebas Unitarias

```bash
# Todas las pruebas
./gradlew test

# Solo pruebas de customer-service
./gradlew :services:customer-service:test

# Solo pruebas de policy-service
./gradlew :services:policy-service:test

# Ver reporte de cobertura
./gradlew test jacocoTestReport
```

## 🐛 Solución de Problemas

### Error: "Permission denied" al ejecutar gradlew en Mac/Linux

**Solución:**
```bash
chmod +x gradlew
```

Este problema ya está resuelto en los Dockerfiles con:
```dockerfile
RUN chmod +x ./gradlew && ./gradlew ...
```

### Error: Puerto ya en uso

Si los puertos 8081 o 8082 están ocupados:

**Opción 1: Detener el proceso que usa el puerto**
```bash
# Windows
netstat -ano | findstr :8081
taskkill /PID <PID> /F

# Mac/Linux
lsof -ti:8081 | xargs kill -9
```

**Opción 2: Cambiar el puerto en docker-compose.yml**
```yaml
services:
  customer-service:
    ports:
      - "9081:8081"  # Cambiar puerto externo
```

### Error: Out of Memory al compilar

Aumentar memoria de Gradle en `gradle.properties`:
```properties
org.gradle.jvmargs=-Xmx2048m
```

## 📊 Endpoints Disponibles

### Customer Service (http://localhost:8081)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/customers` | Crear cliente |
| GET | `/api/customers` | Listar todos los clientes |
| GET | `/api/customers/{id}` | Obtener cliente por ID |
| PUT | `/api/customers/{id}` | Actualizar cliente |
| DELETE | `/api/customers/{id}` | Eliminar cliente |

### Policy Service (http://localhost:8082)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/policies` | Crear póliza |
| GET | `/api/policies` | Listar todas las pólizas |
| GET | `/api/policies/{id}` | Obtener póliza por ID |
| GET | `/api/policies/customer/{customerId}` | Listar pólizas de un cliente |
| GET | `/api/policies/{id}/beneficiaries` | Obtener beneficiarios de una póliza |

## 🔍 Trazabilidad

Cada petición genera un `transactionId` único (UUID) que se incluye en:
- La respuesta de la API
- Los logs del sistema
- La base de datos de trazabilidad

Esto permite rastrear el flujo completo de una operación a través de todos los componentes del sistema.

## 📝 Logs

Para ver los logs en tiempo real:

```bash
# Con Docker
docker-compose logs -f customer-service
docker-compose logs -f policy-service

# Sin Docker
# Los logs aparecen en la consola donde ejecutaste bootRun
```

