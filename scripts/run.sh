#!/bin/bash
# Script de ayuda para Mac/Linux
# Uso: ./scripts/run.sh [comando]

set -e

# Colores
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
CYAN='\033[0;36m'
NC='\033[0m' # No Color

function show_help {
    cat << EOF
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

  ./scripts/run.sh build
  ./scripts/run.sh up
  ./scripts/run.sh test

Documentación:
  - README.md         : Información general del proyecto
  - API-REFERENCE.md  : Referencia rápida de endpoints
  - DEPLOYMENT.md     : Guía de despliegue completa

EOF
}

function build_project {
    echo -e "${CYAN}🔨 Compilando proyecto...${NC}"
    chmod +x ./gradlew
    ./gradlew clean build
    echo -e "${GREEN}✅ Compilación exitosa${NC}"
}

function start_services {
    echo -e "${CYAN}🚀 Levantando servicios con Docker Compose...${NC}"
    docker-compose up --build -d
    echo ""
    echo -e "${GREEN}✅ Servicios iniciados correctamente${NC}"
    echo ""
    echo -e "${YELLOW}📌 URLs disponibles:${NC}"
    echo -e "   Customer Service:  http://localhost:8081"
    echo -e "   Policy Service:    http://localhost:8082"
    echo -e "   Swagger Customer:  http://localhost:8081/swagger-ui.html"
    echo -e "   Swagger Policy:    http://localhost:8082/swagger-ui.html"
    echo -e "   H2 Console Customer: http://localhost:8091"
    echo -e "   H2 Console Policy:   http://localhost:8092"
    echo ""
    echo -e "${CYAN}💡 Ver logs: ./scripts/run.sh logs${NC}"
}

function stop_services {
    echo -e "${CYAN}🛑 Deteniendo servicios...${NC}"
    docker-compose down
    echo -e "${GREEN}✅ Servicios detenidos${NC}"
}

function run_tests {
    echo -e "${CYAN}🧪 Ejecutando pruebas unitarias...${NC}"
    chmod +x ./gradlew
    ./gradlew test
    echo -e "${GREEN}✅ Todas las pruebas pasaron${NC}"
    echo -e "${YELLOW}📊 Ver reportes en:${NC}"
    echo -e "   services/customer-service/build/reports/tests/test/index.html"
    echo -e "   services/policy-service/build/reports/tests/test/index.html"
}

function show_logs {
    echo -e "${CYAN}📋 Mostrando logs de servicios...${NC}"
    echo -e "${YELLOW}💡 Presiona Ctrl+C para salir${NC}"
    docker-compose logs -f
}

function clean_project {
    echo -e "${CYAN}🧹 Limpiando archivos de compilación...${NC}"
    chmod +x ./gradlew
    ./gradlew clean
    echo -e "${GREEN}✅ Limpieza completada${NC}"
}

# Procesar comando
case "${1:-help}" in
    build)
        build_project
        ;;
    up)
        start_services
        ;;
    down)
        stop_services
        ;;
    test)
        run_tests
        ;;
    logs)
        show_logs
        ;;
    clean)
        clean_project
        ;;
    help|*)
        show_help
        ;;
esac

