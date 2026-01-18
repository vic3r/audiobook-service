# Setup Kafka Topics for Audiobook Service
# PowerShell script to create required Kafka topics

$KAFKA_CONTAINER = "audiobook-kafka"
$BOOTSTRAP_SERVER = "localhost:9092"

Write-Host "Setting up Kafka topics..." -ForegroundColor Yellow

# Check if Kafka container is running
$containerRunning = docker ps --filter "name=$KAFKA_CONTAINER" --format "{{.Names}}"
if (-not $containerRunning) {
    Write-Host "Error: Kafka container '$KAFKA_CONTAINER' is not running" -ForegroundColor Red
    Write-Host "Please start Kafka first: docker-compose up -d kafka" -ForegroundColor Yellow
    exit 1
}

# Wait for Kafka to be ready
Write-Host "Waiting for Kafka to be ready..." -ForegroundColor Yellow
Start-Sleep -Seconds 5

# Function to create topic if it doesn't exist
function Create-Topic {
    param(
        [string]$TopicName,
        [int]$Partitions = 3,
        [int]$ReplicationFactor = 1
    )
    
    Write-Host "Creating topic: $TopicName" -ForegroundColor Yellow
    
    docker exec -it $KAFKA_CONTAINER kafka-topics `
        --bootstrap-server localhost:9092 `
        --create `
        --if-not-exists `
        --topic $TopicName `
        --partitions $Partitions `
        --replication-factor $ReplicationFactor `
        --config min.insync.replicas=1
    
    if ($LASTEXITCODE -eq 0) {
        Write-Host "✓ Topic '$TopicName' created successfully" -ForegroundColor Green
    } else {
        Write-Host "✗ Failed to create topic '$TopicName'" -ForegroundColor Red
        return $false
    }
    return $true
}

# Create topics
Create-Topic -TopicName "library-events" -Partitions 3 -ReplicationFactor 1
Create-Topic -TopicName "purchase-events" -Partitions 3 -ReplicationFactor 1

# List all topics
Write-Host "`nListing all topics:" -ForegroundColor Yellow
docker exec -it $KAFKA_CONTAINER kafka-topics `
    --bootstrap-server localhost:9092 `
    --list

Write-Host "`nKafka topics setup completed!" -ForegroundColor Green
Write-Host "Topics created:" -ForegroundColor Yellow
Write-Host "  - library-events (3 partitions, 1 replica)"
Write-Host "  - purchase-events (3 partitions, 1 replica)"
