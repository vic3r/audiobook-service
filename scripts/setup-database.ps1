# Setup Database for Audiobook Service
# PowerShell script to create the PostgreSQL database

$POSTGRES_CONTAINER = "audiobook-postgres"
$DATABASE_NAME = "audiobooks"
$POSTGRES_USER = "postgres"
$POSTGRES_PASSWORD = "postgres"

Write-Host "Setting up database..." -ForegroundColor Yellow

# Check if PostgreSQL container is running
$containerRunning = docker ps --filter "name=$POSTGRES_CONTAINER" --format "{{.Names}}"
if (-not $containerRunning) {
    Write-Host "Error: PostgreSQL container '$POSTGRES_CONTAINER' is not running" -ForegroundColor Red
    Write-Host "Please start PostgreSQL first: docker-compose up -d postgres" -ForegroundColor Yellow
    exit 1
}

# Wait for PostgreSQL to be ready
Write-Host "Waiting for PostgreSQL to be ready..." -ForegroundColor Yellow
Start-Sleep -Seconds 3

# Check if database already exists
$dbExists = docker exec -it $POSTGRES_CONTAINER psql -U $POSTGRES_USER -tAc "SELECT 1 FROM pg_database WHERE datname='$DATABASE_NAME'"

if ($dbExists -eq "1") {
    Write-Host "Database '$DATABASE_NAME' already exists" -ForegroundColor Yellow
} else {
    Write-Host "Creating database '$DATABASE_NAME'..." -ForegroundColor Yellow
    docker exec -it $POSTGRES_CONTAINER psql -U $POSTGRES_USER -c "CREATE DATABASE $DATABASE_NAME;"
    
    if ($LASTEXITCODE -eq 0) {
        Write-Host "✓ Database '$DATABASE_NAME' created successfully" -ForegroundColor Green
    } else {
        Write-Host "✗ Failed to create database '$DATABASE_NAME'" -ForegroundColor Red
        exit 1
    }
}

# Note: Tables are created automatically by Spring Boot JPA (ddl-auto: update)
Write-Host "Note: Tables will be created automatically when Spring Boot starts" -ForegroundColor Yellow
Write-Host "Spring Boot uses ddl-auto: update to create/update tables" -ForegroundColor Yellow

Write-Host "`nDatabase setup completed!" -ForegroundColor Green
Write-Host "Database: $DATABASE_NAME" -ForegroundColor Yellow
Write-Host "Connection: Host=localhost, Port=5432, Database=$DATABASE_NAME, User=$POSTGRES_USER" -ForegroundColor Yellow

# List databases
Write-Host "`nAvailable databases:" -ForegroundColor Yellow
docker exec -it $POSTGRES_CONTAINER psql -U $POSTGRES_USER -c "\l" | Select-String -Pattern "Name|$DATABASE_NAME"
