# 🚀 GitHub Actions CI/CD Workflow

## 🏗️ Estructura del Workflow

### 1️⃣ **Build Job** (Compilación y Tests)
```yaml
- Checkout código
- Setup JDK 21
- Compilar con Gradle
- Ejecutar tests
- Generar JavaDoc
- Subir artefactos (tests, docs)
```

**Triggers:** Push y Pull Request en main/develop  
**Duración:** ~5-10 minutos  
**Outputs:** Test results, JavaDoc, build artifacts

---

### 2️⃣ **Quality Job** (Calidad de Código)
```yaml
- Checkout código
- Setup JDK 21
- Verificar estilo de código
- Generar cobertura (JaCoCo)
- Subir a Codecov (opcional)
```

**Triggers:** Siempre (después de cada commit)  
**Duración:** ~5 minutos  
**Outputs:** Coverage reports, quality metrics

---

### 3️⃣ **Docker Job** (Construcción de Imágenes)
```yaml
- Setup Docker Buildx
- Login a GitHub Container Registry
- Para cada servicio (customer, policy):
  - Extraer metadatos
  - Construir imagen Docker
  - Push a registry
```

**Triggers:** Solo en push a main  
**Duración:** ~10-15 minutos  
**Outputs:** Docker images en GitHub Container Registry

---

### 4️⃣ **Dependency Job** (Análisis de Dependencias)
```yaml
- Checkout código
- Setup JDK 21
- Generar y enviar gráfico de dependencias
```

**Triggers:** Siempre  
**Duración:** ~3 minutos  
**Outputs:** Dependency graph para Dependabot

---

### 5️⃣ **Docs Job** (Publicación de Documentación)
```yaml
- Checkout código
- Setup JDK 21
- Generar JavaDoc
- Subir a GitHub Pages
- Desplegar automáticamente
```

**Triggers:** Solo en push a main  
**Duración:** ~5 minutos  
**Outputs:** Documentación publicada en GitHub Pages

---

## 🔑 Características Implementadas

### ✅ Caché de Gradle
```yaml
cache: gradle
```
- Acelera builds subsecuentes
- Reduce tiempo de ejecución
- Ahorra ancho de banda

### ✅ Matriz de Servicios
```yaml
strategy:
  matrix:
    service: [customer-service, policy-service]
```
- Compila ambos servicios en paralelo
- Escalable a más servicios
- Imágenes Docker separadas

### ✅ Condicionales
```yaml
if: github.event_name == 'push' && github.ref == 'refs/heads/main'
```
- Docker solo en main
- Documentación solo en main
- Ahorra recursos

### ✅ Continuación en Errores
```yaml
continue-on-error: true
```
- JavaDoc: No falla el workflow
- Coverage: Informativo
- No detiene despliegue

### ✅ Artifacts
```yaml
uses: actions/upload-artifact@v4
```
- Guarda test results
- Guarda JavaDoc
- Disponible por 90 días
- Descargable desde UI

---