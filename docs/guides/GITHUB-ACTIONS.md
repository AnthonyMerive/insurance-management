# 🚀 GitHub Actions CI/CD Workflow

## ✅ Workflow Implementado

Se ha creado un workflow completo en `.github/workflows/ci-cd.yml` optimizado para tu proyecto.

---

## 📋 Comparación: Workflow Sugerido vs Implementado

### ❌ Problemas del Workflow Sugerido

| Problema | Impacto |
|----------|---------|
| **Java 17** | Tu proyecto necesita Java 21 LTS |
| **Sin validación código** | No detecta problemas de calidad |
| **Sin JavaDoc** | No genera documentación |
| **Sin Docker** | No construye imágenes para servicios |
| **Sin cobertura código** | No mide test coverage |
| **Limitado** | Solo build y test básicos |

### ✅ Mejoras Implementadas

| Mejora | Beneficio |
|--------|-----------|
| **Java 21** | Versión correcta del proyecto |
| **Multi-job** | Ejecución paralela de tareas |
| **JavaDoc** | Genera documentación automáticamente |
| **Docker Build** | Construye imágenes para cada servicio |
| **Code Coverage** | Mide calidad del código |
| **GitHub Pages** | Publica documentación automáticamente |
| **Matriz de servicios** | Compilar ambos servicios en paralelo |
| **Artifacts** | Guarda resultados (tests, docs, reports) |

---

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

## 📊 Comparativa de Ejecución

### Workflow Sugerido
```
Tiempo total: ~15-20 minutos
┌─────────────────────┐
│ Setup JDK 17        │
│ Build               │
│ (sin tests)         │
│ Dependabot submit   │
└─────────────────────┘
```

### Workflow Implementado
```
Tiempo total: ~20-25 minutos (con paralelización)
┌─────────────────────────────────────────────┐
│ Build Job (15 min)    Quality Job (5 min)   │
│ - Compilar            - Check style         │
│ - Tests               - Coverage            │
│ - JavaDoc             - Upload Codecov      │
├─────────────────────────────────────────────┤
│ Docker Job (15 min)    Dependency Job (3m)  │
│ - Build images        - Submit dependency   │
│ - Push to registry                          │
├─────────────────────────────────────────────┤
│ Docs Job (5 min)                            │
│ - Generate & Deploy JavaDoc                 │
└─────────────────────────────────────────────┘
```

---

## 🔧 Configuración Requerida

### 1. GitHub Container Registry (GHCR)
Ya está disponible por defecto en tu organización.

### 2. GitHub Pages (Opcional)
Para publicar documentación:

1. Ve a Settings → Pages
2. Source: Deploy from a branch
3. Branch: gh-pages
4. Directory: root

### 3. Codecov (Opcional)
Para integración de coverage:

1. Ve a https://codecov.io
2. Conecta tu repositorio
3. Copia el token
4. Agrega a Secrets: `CODECOV_TOKEN`

---

## 📝 Variables de Entorno

```yaml
env:
  REGISTRY: ghcr.io                    # GitHub Container Registry
  IMAGE_NAME: ${{ github.repository }} # tu-usuario/proyecto
```

### Modificar si necesario

Para usar Docker Hub en lugar de GHCR:
```yaml
env:
  REGISTRY: docker.io
  IMAGE_NAME: ${{ secrets.DOCKERHUB_USERNAME }}/insurance-management
```

---

## 🔐 Secretos Necesarios

### Automáticos (GitHub)
- `GITHUB_TOKEN` - Ya disponible

### Opcionales (Agregables)
```
Ir a Settings → Secrets and variables → Actions
```

| Secreto | Para | Necesario |
|---------|------|-----------|
| `CODECOV_TOKEN` | Codecov integration | ❌ No |
| `DOCKERHUB_USERNAME` | Docker Hub | ❌ No |
| `DOCKERHUB_TOKEN` | Docker Hub | ❌ No |

---

## 🚀 Cómo Usar

### Activar el Workflow
1. Commit y push a `main` o `develop`
2. Ve a la pestaña "Actions" en GitHub
3. El workflow se ejecutará automáticamente

### Ver Resultados
1. **Build Status:** Badge en README
2. **Test Results:** En artifacts
3. **JavaDoc:** Deployed en GitHub Pages
4. **Docker Images:** En Container Registry
5. **Dependency Graph:** En Security tab

### Descargar Artifacts
1. Haz click en el workflow
2. Baja en "Artifacts"
3. Descarga los que necesites

---

## 📊 Monitoreo

### GitHub Actions Dashboard
- Muestra estado de cada job
- Tiempo de ejecución
- Logs detallados
- Re-ejecutar si es necesario

### Notificaciones
GitHub notifica automáticamente si hay fallos en:
- El commit
- Pull requests
- Email (configurable)

---

## 🎯 Próximas Mejoras

### Fáciles
```yaml
- Agregar Codecov integration
- Slack notifications en fallos
- Auto-merge en PR exitosos
```

### Intermedias
```yaml
- SAST scan (SonarQube, Snyk)
- Dependency scanning
- Container scanning
```

### Avanzadas
```yaml
- ArgoCD deployment
- Terraform provisioning
- Helm charts release
```

---

## ⚠️ Consideraciones Importantes

### 1. **Permisos**
Asegúrate que el repo tiene permisos para:
- Escribir en Container Registry
- Leer dependencias
- Escribir en Pages

### 2. **Recursos**
- GitHub Actions: 2000 minutos gratis/mes (usuarios)
- Este workflow: ~100 min/mes por push
- No hay problema para uso moderado

### 3. **Seguridad**
- GHCR requiere token automático (seguro)
- Docker Hub requiere credentials (configurar)
- Nunca commits secretos en el repo

### 4. **Performance**
- Caché acelera builds
- Paralelización reduce tiempo
- ~20-25 min es normal

---

## 🆘 Troubleshooting

### El workflow no se ejecuta
- ✅ Verifica que el archivo esté en `.github/workflows/ci-cd.yml`
- ✅ Haz un push a main o develop
- ✅ Ve a Actions en GitHub

### Build falla
- ✅ Ve los logs de GitHub Actions
- ✅ Verifica Java 21 disponible
- ✅ Ejecuta localmente: `./gradlew build`

### Docker push falla
- ✅ Verifica permisos en GHCR
- ✅ Asegúrate que el token existe (automático)
- ✅ Revisa los logs

### JavaDoc no se publica
- ✅ Habilita GitHub Pages en Settings
- ✅ Verifica rama gh-pages
- ✅ Espera a que el job termine

---

## 📚 Documentos Relacionados

Para más información:
- [GitHub Actions Docs](https://docs.github.com/en/actions)
- [Gradle Wrapper Guide](https://docs.gradle.org/current/userguide/gradle_wrapper.html)
- [Docker Build Action](https://github.com/docker/build-push-action)
- [Java Setup Action](https://github.com/actions/setup-java)

---

**Workflow Implementado:** ✅ `.github/workflows/ci-cd.yml`  
**Estado:** Listo para usar  
**Fecha:** 2 de Marzo, 2026

