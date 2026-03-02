# Script de ayuda para Windows PowerShell
# Uso: .\scripts\run.ps1 [comando]

param(
    [Parameter(Position=0)]
    [ValidateSet('build', 'up', 'down', 'test', 'logs', 'clean', 'help')]
    [string]$Command = 'help'
)

function Show-Help {
    Write-Host @"
╔══════════════════════════════════════════════════════════════╗
║     Sistema de Gestión de Seguros - Script de Ayuda        ║
╚══════════════════════════════════════════════════════════════╝

Comandos disponibles:

  build     - Compilar el proyecto con Gradle
  up        - Levantar servicios con Docker Compose
  down      - Detener servicios de Docker Compose
  test      - Ejecutar pruebas unitarias
  logs      - Ver logs de los servicios en tiempo real
  clean     - Limpiar archivos de compilación
  help      - Mostrar esta ayuda

Ejemplos:

  .\scripts\run.ps1 build
  .\scripts\run.ps1 up
  .\scripts\run.ps1 test

Documentación:
  - README.md         : Información general del proyecto
  - API-REFERENCE.md  : Referencia rápida de endpoints
  - DEPLOYMENT.md     : Guía de despliegue completa

"@
}

function Build-Project {
    Write-Host "🔨 Compilando proyecto..." -ForegroundColor Cyan
    .\gradlew.bat clean build
    if ($LASTEXITCODE -eq 0) {
        Write-Host "✅ Compilación exitosa" -ForegroundColor Green
    } else {
        Write-Host "❌ Error en la compilación" -ForegroundColor Red
        exit 1
    }
}

function Start-Services {
    Write-Host "🚀 Levantando servicios con Docker Compose..." -ForegroundColor Cyan
    docker-compose up --build -d
    if ($LASTEXITCODE -eq 0) {
        Write-Host ""
        Write-Host "✅ Servicios iniciados correctamente" -ForegroundColor Green
        Write-Host ""
        Write-Host "📌 URLs disponibles:" -ForegroundColor Yellow
        Write-Host "   Customer Service:  http://localhost:8081" -ForegroundColor White
        Write-Host "   Policy Service:    http://localhost:8082" -ForegroundColor White
        Write-Host "   Swagger Customer:  http://localhost:8081/swagger-ui.html" -ForegroundColor White
        Write-Host "   Swagger Policy:    http://localhost:8082/swagger-ui.html" -ForegroundColor White
        Write-Host "   H2 Console Customer: http://localhost:8091" -ForegroundColor White
        Write-Host "   H2 Console Policy:   http://localhost:8092" -ForegroundColor White
        Write-Host ""
        Write-Host "💡 Ver logs: .\scripts\run.ps1 logs" -ForegroundColor Cyan
    } else {
        Write-Host "❌ Error al iniciar servicios" -ForegroundColor Red
        exit 1
    }
}

function Stop-Services {
    Write-Host "🛑 Deteniendo servicios..." -ForegroundColor Cyan
    docker-compose down
    if ($LASTEXITCODE -eq 0) {
        Write-Host "✅ Servicios detenidos" -ForegroundColor Green
    } else {
        Write-Host "❌ Error al detener servicios" -ForegroundColor Red
        exit 1
    }
}

function Run-Tests {
    Write-Host "🧪 Ejecutando pruebas unitarias..." -ForegroundColor Cyan
    .\gradlew.bat test
    if ($LASTEXITCODE -eq 0) {
        Write-Host "✅ Todas las pruebas pasaron" -ForegroundColor Green
        Write-Host "📊 Ver reportes en:" -ForegroundColor Yellow
        Write-Host "   services/customer-service/build/reports/tests/test/index.html" -ForegroundColor White
        Write-Host "   services/policy-service/build/reports/tests/test/index.html" -ForegroundColor White
    } else {
        Write-Host "❌ Algunas pruebas fallaron" -ForegroundColor Red
        exit 1
    }
}

function Show-Logs {
    Write-Host "📋 Mostrando logs de servicios..." -ForegroundColor Cyan
    Write-Host "💡 Presiona Ctrl+C para salir" -ForegroundColor Yellow
    docker-compose logs -f
}

function Clean-Project {
    Write-Host "🧹 Limpiando archivos de compilación..." -ForegroundColor Cyan
    .\gradlew.bat clean
    if ($LASTEXITCODE -eq 0) {
        Write-Host "✅ Limpieza completada" -ForegroundColor Green
    } else {
        Write-Host "❌ Error en la limpieza" -ForegroundColor Red
        exit 1
    }
}

# Ejecutar comando
switch ($Command) {
    'build' { Build-Project }
    'up'    { Start-Services }
    'down'  { Stop-Services }
    'test'  { Run-Tests }
    'logs'  { Show-Logs }
    'clean' { Clean-Project }
    'help'  { Show-Help }
    default { Show-Help }
}

