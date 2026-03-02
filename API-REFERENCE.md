# 📋 Referencia Rápida de API

## 🔗 URLs Base

- **Customer Service**: `http://localhost:8081`
- **Policy Service**: `http://localhost:8082`

---

## 👤 Customer Service API

### 1. Crear Cliente
```http
POST /api/customers
Content-Type: application/json

{
  "firstName": "Ana",
  "lastName": "Perez",
  "email": "ana.perez@example.com",
  "phoneNumber": "+57-300-000-0000",
  "birthDate": "1990-01-15"
}
```

**Respuesta (201 Created):**
```json
{
  "transactionId": "550e8400-e29b-41d4-a716-446655440000",
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

### 2. Listar Todos los Clientes
```http
GET /api/customers
```

**Respuesta (200 OK):**
```json
{
  "transactionId": "uuid",
  "message": "success",
  "code": 200,
  "data": [
    {
      "id": 1,
      "firstName": "Ana",
      "lastName": "Perez",
      "email": "ana.perez@example.com",
      "phoneNumber": "+57-300-000-0000",
      "birthDate": "1990-01-15"
    }
  ]
}
```

### 3. Obtener Cliente por ID
```http
GET /api/customers/{id}
```

**Ejemplo:**
```http
GET /api/customers/1
```

### 4. Actualizar Cliente
```http
PUT /api/customers/{id}
Content-Type: application/json

{
  "firstName": "Ana",
  "lastName": "Perez Martinez",
  "email": "ana.perez.updated@example.com",
  "phoneNumber": "+57-300-111-1111",
  "birthDate": "1990-01-15"
}
```

### 5. Eliminar Cliente
```http
DELETE /api/customers/{id}
```

**Respuesta (204 No Content)**

---

## 📄 Policy Service API

### 1. Crear Póliza
```http
POST /api/policies
Content-Type: application/json

{
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
}
```

**Tipos de Póliza:**
- `VIDA`: Seguro de vida
- `VEHICULO`: Seguro de vehículos
- `SALUD`: Seguro de salud

**Respuesta (201 Created):**
```json
{
  "transactionId": "uuid",
  "message": "success",
  "code": 201,
  "data": {
    "id": 1,
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
  }
}
```

### 2. Listar Todas las Pólizas
```http
GET /api/policies
```

### 3. Obtener Póliza por ID
```http
GET /api/policies/{id}
```

**Ejemplo:**
```http
GET /api/policies/1
```

### 4. Obtener Pólizas de un Cliente
```http
GET /api/policies/customer/{customerId}
```

**Ejemplo:**
```http
GET /api/policies/customer/1
```

### 5. Obtener Beneficiarios de una Póliza
```http
GET /api/policies/{id}/beneficiaries
```

**Ejemplo:**
```http
GET /api/policies/1/beneficiaries
```

**Respuesta (200 OK):**
```json
{
  "transactionId": "uuid",
  "message": "success",
  "code": 200,
  "data": [
    {
      "firstName": "Luis",
      "lastName": "Gomez",
      "dni": "12345678"
    }
  ]
}
```

---

## ⚠️ Respuestas de Error

### Error de Validación (400 Bad Request)
```json
{
  "transactionId": "uuid",
  "message": "Error de validación: email es requerido",
  "code": 400,
  "data": null
}
```

### Recurso No Encontrado (404 Not Found)
```json
{
  "transactionId": "uuid",
  "message": "Cliente no encontrado con ID: 999",
  "code": 404,
  "data": null
}
```

### Error del Servidor (500 Internal Server Error)
```json
{
  "transactionId": "uuid",
  "message": "Error interno del servidor",
  "code": 500,
  "data": null
}
```

---

## 🔍 Reglas de Negocio - Pólizas

### VIDA
- ✅ Debe tener al menos 1 beneficiario
- ✅ `beneficiaries` es obligatorio y no puede estar vacío
- ❌ No aplica `insuredVehicles`

### VEHICULO
- ✅ Debe tener al menos 1 vehículo asegurado
- ✅ `insuredVehicles` es obligatorio y no puede estar vacío
- ❌ No aplica `beneficiaries`

### SALUD
- ✅ Puede incluir padres adicionales (`extraParents`)
- ✅ Puede incluir hijos adicionales (`extraChildren`)
- ✅ Puede incluir cónyuge (`hasSpouse`)
- ❌ No aplica `beneficiaries` ni `insuredVehicles`

---

## 📊 Códigos de Estado HTTP

| Código | Descripción | Cuándo se usa |
|--------|-------------|---------------|
| 200 | OK | Operación exitosa (GET, PUT) |
| 201 | Created | Recurso creado exitosamente (POST) |
| 204 | No Content | Recurso eliminado exitosamente (DELETE) |
| 400 | Bad Request | Datos inválidos o reglas de negocio no cumplidas |
| 404 | Not Found | Recurso no encontrado |
| 500 | Internal Server Error | Error del servidor |

---

## 🧪 Ejemplos con cURL

### Crear Cliente
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

### Listar Clientes
```bash
curl http://localhost:8081/api/customers
```

### Crear Póliza de Vida
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
    ]
  }'
```

### Crear Póliza de Vehículo
```bash
curl -X POST http://localhost:8082/api/policies \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": 1,
    "type": "VEHICULO",
    "insuredVehicles": ["ABC123", "XYZ789"]
  }'
```

### Crear Póliza de Salud
```bash
curl -X POST http://localhost:8082/api/policies \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": 1,
    "type": "SALUD",
    "extraParents": 2,
    "extraChildren": 3,
    "hasSpouse": true
  }'
```

---

## 📚 Documentación Adicional

- **OpenAPI Spec**: `docs/api/openapi.yml`
- **Postman Collection**: `docs/api/insurance-management.postman_collection.json`
- **Swagger UI Customer**: http://localhost:8081/swagger-ui.html
- **Swagger UI Policy**: http://localhost:8082/swagger-ui.html

