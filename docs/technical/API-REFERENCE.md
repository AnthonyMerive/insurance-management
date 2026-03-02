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
  "transactionId": "uuid",
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

**Respuesta (200 OK):** Array de clientes

### 3. Obtener Cliente por ID
```http
GET /api/customers/1
```

### 4. Actualizar Cliente
```http
PUT /api/customers/1
Content-Type: application/json

{
  "firstName": "Ana",
  "lastName": "Perez Martinez",
  "email": "ana.updated@example.com"
}
```

### 5. Eliminar Cliente
```http
DELETE /api/customers/1
```

**Respuesta:** 204 No Content

---

## 📄 Policy Service API

### 1. Crear Póliza

#### Póliza VIDA (Con Beneficiarios)
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
      "documentNumber": "12345678"
    }
  ]
}
```

#### Póliza VEHICULO (Con Vehículos)
```http
{
  "customerId": 1,
  "type": "VEHICULO",
  "insuredVehicles": ["ABC123", "XYZ789"]
}
```

#### Póliza SALUD (Con Familiares)
```http
{
  "customerId": 1,
  "type": "SALUD",
  "extraParents": 2,
  "extraChildren": 1,
  "hasSpouse": true
}
```

**Respuesta (201 Created):** Póliza creada con ID

### 2. Listar Pólizas por Cliente
```http
GET /api/policies/customer/1
```

### 3. Obtener Póliza por ID
```http
GET /api/policies/1
```

### 4. Obtener Beneficiarios de una Póliza
```http
GET /api/policies/1/beneficiaries
```

**Respuesta (200 OK):** Array de beneficiarios

---

## ⚠️ Respuestas de Error

### Validación Fallida (400 Bad Request)
```json
{
  "transactionId": "uuid",
  "message": "Validación fallida",
  "code": 400,
  "data": null
}
```

### Recurso No Encontrado (404 Not Found)
```json
{
  "transactionId": "uuid",
  "message": "Cliente no encontrado",
  "code": 404,
  "data": null
}
```

### Error del Servidor (500 Internal Server Error)
```json
{
  "transactionId": "uuid",
  "message": "Error interno",
  "code": 500,
  "data": null
}
```

---

## 🔍 Reglas de Negocio

### Póliza VIDA
- ✅ Solo una por cliente
- ✅ Máximo 2 beneficiarios
- ✅ Beneficiarios obligatorios

### Póliza VEHICULO
- ✅ Múltiples por cliente
- ✅ Mínimo 1 vehículo
- ✅ Identificados por placa

### Póliza SALUD
- ✅ Múltiples por cliente
- ✅ Padres adicionales (0-2)
- ✅ Hijos adicionales (0-N)
- ✅ Cónyuge (sí/no)

---

## 📊 Códigos de Estado HTTP

| Código | Descripción | Cuándo |
|--------|-------------|--------|
| **200** | OK | GET exitoso |
| **201** | Created | POST exitoso |
| **204** | No Content | DELETE exitoso |
| **400** | Bad Request | Datos inválidos |
| **404** | Not Found | Recurso no existe |
| **500** | Server Error | Error del servidor |

---

## 🧪 Ejemplos con cURL

### Crear Cliente
```bash
curl -X POST http://localhost:8081/api/customers \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Ana",
    "lastName": "Perez",
    "email": "ana@example.com",
    "phoneNumber": "+57-300-000-0000",
    "birthDate": "1990-01-15"
  }'
```

### Crear Póliza VIDA
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
        "documentNumber": "12345678"
      }
    ]
  }'
```

### Listar Clientes
```bash
curl http://localhost:8081/api/customers
```

### Obtener Pólizas de Cliente
```bash
curl http://localhost:8082/api/policies/customer/1
```

---

## 📚 Recursos Adicionales

- **OpenAPI Spec:** `../api/openapi.yml`
- **Postman Collection:** `../api/insurance-management.postman_collection.json`
- **Swagger UI Customer:** `http://localhost:8081/swagger-ui.html`
- **Swagger UI Policy:** `http://localhost:8082/swagger-ui.html`