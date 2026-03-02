# 🎯 Índice de Documentación - Sistema de Gestión de Seguros

## 🔗 Empezar Aquí

¡Bienvenido al proyecto de **Sistema de Gestión de Seguros**! Este índice te ayudará a navegar toda la documentación.

---

## 📚 Documentación Principal

### 1. **DEPLOYMENT.md** ⭐ PARA EJECUTAR
- Cómo levantar los servicios
- Prerequisitos del sistema
- Acceso a herramientas
- Solución de problemas

👉 **Archivo:** [DEPLOYMENT.md](DEPLOYMENT.md)

### 2. **QUICK-COMMANDS.md** - Comandos Útiles
- Comandos esenciales
- Ejemplos con cURL
- Docker Compose
- Gradle

👉 **Archivo:** [QUICK-COMMANDS.md](QUICK-COMMANDS.md)

### 3. **GITHUB-ACTIONS.md** - CI/CD Pipeline
- Workflow automático
- Comparación y mejoras
- Configuración
- Monitoreo

👉 **Archivo:** [GITHUB-ACTIONS.md](GITHUB-ACTIONS.md)

---

## 📖 Documentación Técnica

### 3. **JAVADOC-SUMMARY.md** - Documentación del Código
- Clases documentadas
- Métodos explicados
- Cómo generar JavaDoc
- Estadísticas

👉 **Archivo:** [../technical/JAVADOC-SUMMARY.md](../technical/JAVADOC-SUMMARY.md)

### 4. **API-REFERENCE.md** - Referencia de Endpoints
- URLs de servicios
- Documentación de endpoints
- Ejemplos request/response
- Reglas de negocio

👉 **Archivo:** [../technical/API-REFERENCE.md](../technical/API-REFERENCE.md)

### 5. **ARCHITECTURE.md** - Detalles de Arquitectura
- Arquitectura hexagonal
- Capas del sistema
- Flujo de datos
- Tecnologías

👉 **Archivo:** [../technical/ARCHITECTURE.md](../technical/ARCHITECTURE.md)

---

## 📊 Recursos

### API Documentation
- **OpenAPI Spec:** `../api/openapi.yml`
- **Postman Collection:** `../api/insurance-management.postman_collection.json`

### Diagramas
- **Arquitectura Local:** `../diagrams/architecture.xml`
- **Modelo de Datos:** `../diagrams/data-model.xml`
- **Arquitectura AWS:** `../diagrams/aws-architecture.xml`

### Imágenes
- **architecture.png** - Diagrama visual local
- **data-model.png** - Diagrama visual de datos
- **aws-architecture.png** - Diagrama visual AWS

---

## 🎯 Por Rol

### 👨‍💻 Desarrollador Nuevo
1. Lee [DEPLOYMENT.md](DEPLOYMENT.md)
2. Ejecuta los servicios
3. Consulta [../technical/JAVADOC-SUMMARY.md](../technical/JAVADOC-SUMMARY.md)
4. Genera JavaDoc: `./gradlew javadoc`

### 🏗️ Arquitecto
1. Revisa [../technical/ARCHITECTURE.md](../technical/ARCHITECTURE.md)
2. Ve los diagramas en `../diagrams/`
3. Consulta [../technical/API-REFERENCE.md](../technical/API-REFERENCE.md)

### 🧪 QA/Tester
1. Lee [DEPLOYMENT.md](DEPLOYMENT.md)
2. Consulta [../technical/API-REFERENCE.md](../technical/API-REFERENCE.md)
3. Importa colección en Postman

### 🔧 DevOps
1. Lee [DEPLOYMENT.md](DEPLOYMENT.md)
2. Usa [QUICK-COMMANDS.md](QUICK-COMMANDS.md)
3. Revisa docker-compose.yml

---

## ✅ Checklist de Inicio

- [ ] Tengo Java 21, Docker, Docker Compose
- [ ] He leído DEPLOYMENT.md
- [ ] He levantado los servicios
- [ ] He accedido a Swagger UI
- [ ] He probado algunos endpoints

---

**¡Comienza a trabajar!** 🚀

**Última actualización:** 2 de Marzo, 2026

