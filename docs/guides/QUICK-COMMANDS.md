# 🚀 Guía Rápida de Comandos

## ⚡ Comandos Esenciales

### 📖 Generar JavaDoc

```bash
# Proyecto completo
./gradlew javadoc

# Solo customer-service
./gradlew :services:customer-service:javadoc

# Solo policy-service
./gradlew :services:policy-service:javadoc

# Limpiar y regenerar
./gradlew clean javadoc
```

### 🐳 Docker Compose

```bash
# Levantar servicios (en background)
docker-compose up -d --build

# Levantar servicios (modo interactivo)
docker-compose up --build

# Detener servicios
docker-compose down

# Ver logs
docker-compose logs -f

# Ver logs de un servicio específico
docker-compose logs -f customer-service
docker-compose logs -f policy-service

# Reiniciar servicios
docker-compose restart
```

### 🏗️ Gradle

```bash
# Compilar
./gradlew build

# Compilar sin tests
./gradlew build -x test

# Limpiar
./gradlew clean

# Ejecutar tests
./gradlew test

# Compilar un servicio específico
./gradlew :services:customer-service:build
./gradlew :services:policy-service:build

# Listar tareas disponibles
./gradlew tasks
```

---

## 🪟 Scripts Windows PowerShell

### Usando run.ps1

```powershell
# Compilar proyecto
.\scripts\run.ps1 build

# Levantar servicios
.\scripts\run.ps1 up

# Detener servicios
.\scripts\run.ps1 down

# Ver logs
.\scripts\run.ps1 logs

# Ejecutar tests
.\scripts\run.ps1 test

# Limpiar build
.\scripts\run.ps1 clean

# Ver ayuda
.\scripts\run.ps1 help

# Sin parámetro (muestra ayuda)
.\scripts\run.ps1
```

---

## 🐧 Scripts Mac/Linux

### Usando run.sh

```bash
# Dar permisos (solo primera vez)
chmod +x scripts/run.sh

# Compilar proyecto
./scripts/run.sh build

# Levantar servicios
./scripts/run.sh up

# Detener servicios
./scripts/run.sh down

# Ver logs
./scripts/run.sh logs

# Ejecutar tests
./scripts/run.sh test

# Limpiar build
./scripts/run.sh clean

# Ver ayuda
./scripts/run.sh help

# Sin parámetro (muestra ayuda)
./scripts/run.sh
```

---

## 🔌 cURL - Pruebas Rápidas

### Customer Service

```bash
# Crear cliente
curl -X POST http://localhost:8081/api/customers \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Ana",
    "lastName": "Perez",
    "email": "ana@example.com",
    "phoneNumber": "+57-300-000-0000",
    "birthDate": "1990-01-15"
  }'

# Listar clientes
curl http://localhost:8081/api/customers

# Obtener cliente por ID
curl http://localhost:8081/api/customers/1

# Actualizar cliente
curl -X PUT http://localhost:8081/api/customers/1 \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Ana",
    "lastName": "Perez Martinez",
    "email": "ana.updated@example.com"
  }'

# Eliminar cliente
curl -X DELETE http://localhost:8081/api/customers/1
```

### Policy Service

```bash
# Crear póliza VIDA
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

# Crear póliza VEHICULO
curl -X POST http://localhost:8082/api/policies \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": 1,
    "type": "VEHICULO",
    "insuredVehicles": ["ABC123", "XYZ789"]
  }'

# Crear póliza SALUD
curl -X POST http://localhost:8082/api/policies \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": 1,
    "type": "SALUD",
    "extraParents": 2,
    "extraChildren": 1,
    "hasSpouse": true
  }'

# Listar pólizas de un cliente
curl http://localhost:8082/api/policies/customer/1

# Obtener póliza por ID
curl http://localhost:8082/api/policies/1

# Obtener beneficiarios de una póliza
curl http://localhost:8082/api/policies/1/beneficiaries
```

---

## 📊 URLs de Acceso

### Servicios
- **Customer Service:** http://localhost:8081
- **Policy Service:** http://localhost:8082

### Documentación Swagger
- **Customer Swagger:** http://localhost:8081/swagger-ui.html
- **Policy Swagger:** http://localhost:8082/swagger-ui.html

### Consolas H2
- **Customer H2:** http://localhost:8091
- **Policy H2:** http://localhost:8092

**Credenciales H2:**
- Usuario: `sa`
- Password: `password`

---

## 📝 Documentación Generada

### Después de ejecutar `./gradlew javadoc`

```bash
# Abrir Customer Service JavaDoc
open services/customer-service/build/docs/javadoc/index.html

# Abrir Policy Service JavaDoc
open services/policy-service/build/docs/javadoc/index.html

# En Linux
xdg-open services/customer-service/build/docs/javadoc/index.html

# En Windows
start services\customer-service\build\docs\javadoc\index.html
```

---

## 🔍 Búsqueda en Código

```bash
# Buscar una clase
grep -r "class CustomerService" services/

# Buscar métodos
grep -r "public Mono" services/

# Buscar JavaDoc
grep -r "@param" services/ | head -20

# Buscar excepciones
grep -r "BusinessException" services/
```

---

## 📋 Estados de Verificación

### Verificar que todo compila
```bash
./gradlew clean build
```

### Verificar que los tests pasan
```bash
./gradlew test
```

### Verificar JavaDoc sin errores
```bash
./gradlew javadoc
```

### Verificar todo juntos
```bash
./gradlew clean build javadoc
```

---

## 🐳 Docker - Debugging

```bash
# Ver procesos en ejecución
docker-compose ps

# Ejecutar comando en contenedor
docker-compose exec customer-service bash
docker-compose exec policy-service bash

# Ver logs con filtro
docker-compose logs --tail=100 customer-service
docker-compose logs --tail=100 policy-service

# Detener y eliminar volúmenes
docker-compose down -v

# Rebuild sin cache
docker-compose up --build --no-cache
```

---

## 🔧 Troubleshooting Rápido

### Puerto ya en uso
```bash
# Windows
netstat -ano | findstr :8081
taskkill /PID <PID> /F

# Mac/Linux
lsof -ti:8081 | xargs kill -9
```

### Limpiar todo
```bash
# Gradle
./gradlew clean

# Docker
docker-compose down -v
docker system prune -a

# Entonces reiniciar
docker-compose up --build
```

### Regenerar documentación
```bash
./gradlew clean javadoc
```

---

## 📚 Documentación Rápida

| Documento | Propósito | Comando para Generar |
|-----------|-----------|---------------------|
| JavaDoc HTML | Documentación de código | `./gradlew javadoc` |
| OpenAPI Spec | Especificación API | En `docs/api/openapi.yml` |
| Postman Collection | Endpoints para probar | En `docs/api/insurance-management.postman_collection.json` |

---

## ✅ Checklist de Inicio

```bash
# 1. Clonar y entrar al directorio
cd insurance-management

# 2. Dar permisos (Linux/Mac)
chmod +x scripts/run.sh gradlew

# 3. Compilar
./gradlew clean build

# 4. Levantar servicios
docker-compose up --build

# 5. Generar JavaDoc (en otra terminal)
./gradlew javadoc

# 6. Abrir documentación
# services/customer-service/build/docs/javadoc/index.html

# 7. Probar endpoint
curl http://localhost:8081/api/customers

# ¡Listo!
```

---

## 🎯 Workflow Típico de Desarrollo

```bash
# 1. Actualizar código
# ... editar archivos ...

# 2. Ejecutar tests
./gradlew test

# 3. Compilar
./gradlew build

# 4. Generar JavaDoc
./gradlew javadoc

# 5. Levantar servicios
docker-compose up --build

# 6. Probar endpoints
curl http://localhost:8081/api/customers

# 7. Ver logs
docker-compose logs -f
```

---

## 🚀 Deploy en Producción

```bash
# Compilar sin tests
./gradlew build -x test

# Generar imágenes Docker
docker-compose build --no-cache

# Levantar en producción
docker-compose -f docker-compose.yml up -d

# Ver estado
docker-compose ps

# Ver logs
docker-compose logs -f
```

---

## 📊 Monitoreo

```bash
# Memoria
docker stats

# Procesos
docker ps

# Imágenes
docker images

# Redes
docker network ls

# Volúmenes
docker volume ls
```

---

## 🔐 Seguridad

```bash
# Actualizar imágenes base
docker pull eclipse-temurin:21-jdk-alpine
docker pull eclipse-temurin:21-jre-alpine

# Limpiar recursos no usados
docker system prune -a

# Verificar vulnerabilidades
docker run --rm aquasec/trivy image eclipse-temurin:21-jdk-alpine
```

---

## 📞 Obtener Ayuda

```bash
# Ver tareas disponibles
./gradlew tasks

# Ver ayuda de un comando
./scripts/run.ps1 help
./scripts/run.sh help

# Ver documentación
cat docs/guides/INDEX.md
cat docs/technical/JAVADOC-SUMMARY.md
cat docs/technical/API-REFERENCE.md
cat docs/guides/DEPLOYMENT.md
```

---

**¡Guarde este documento para referencia rápida!** 📌

---

**Actualizado:** 2 de Marzo, 2026

