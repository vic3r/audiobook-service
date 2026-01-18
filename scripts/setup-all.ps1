# Complete Setup Script for Audiobook Service
# PowerShell script to set up both database and Kafka topics

$SCRIPT_DIR = Split-Path -Parent $MyInvocation.MyCommand.Path
$PROJECT_DIR = Split-Path -Parent $SCRIPT_DIR

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  Audiobook Service Setup" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Check if Docker is running
try {
    docker ps | Out-Null
} catch {
    Write-Host "Error: Docker is not running" -ForegroundColor Red
    exit 1
}

# Start infrastructure services
Write-Host "Starting infrastructure services..." -ForegroundColor Yellow
Set-Location $PROJECT_DIR
docker-compose up -d postgres redis zookeeper kafka

# Wait for services to be ready
Write-Host "Waiting for services to be ready..." -ForegroundColor Yellow
Start-Sleep -Seconds 10

# Setup database
Write-Host "`n--- Setting up Database ---" -ForegroundColor Cyan
& "$SCRIPT_DIR\setup-database.ps1"

# Setup Kafka topics
Write-Host "`n--- Setting up Kafka Topics ---" -ForegroundColor Cyan
& "$SCRIPT_DIR\setup-kafka-topics.ps1"

Write-Host "`n========================================" -ForegroundColor Green
Write-Host "  Setup Complete!" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Green
Write-Host ""

Write-Host "Next steps:" -ForegroundColor Yellow
Write-Host "1. Start the Spring Boot application:"
Write-Host "   cd $PROJECT_DIR"
Write-Host "   mvn spring-boot:run"
Write-Host ""
Write-Host "2. Or build and run with Docker:"
Write-Host "   docker-compose up -d --build api"
Write-Host ""
Write-Host "3. Access the API:"
Write-Host "   - API: http://localhost:8080"
Write-Host "   - Swagger UI: http://localhost:8080/swagger-ui.html"
