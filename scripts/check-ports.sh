#!/bin/bash

# Check which ports are in use and by what

echo "=== Checking Port Usage ==="
echo ""

PORTS=("2181:Zookeeper" "9092:Kafka" "5432:PostgreSQL" "6379:Redis")

for PORT_INFO in "${PORTS[@]}"; do
    IFS=':' read -r PORT NAME <<< "$PORT_INFO"
    echo "Checking port $PORT ($NAME)..."
    
    # Check Docker containers
    DOCKER_CONTAINER=$(docker ps --format "{{.Names}}\t{{.Ports}}" 2>/dev/null | grep ":$PORT->" | awk '{print $1}' || true)
    
    if [ ! -z "$DOCKER_CONTAINER" ]; then
        echo "  ⚠ Port $PORT is used by Docker container: $DOCKER_CONTAINER"
        echo "     To stop: docker stop $DOCKER_CONTAINER"
    else
        # Check processes
        PID=$(lsof -ti :$PORT 2>/dev/null || true)
        if [ ! -z "$PID" ]; then
            PROCESS=$(ps -p $PID -o comm= 2>/dev/null || echo "unknown")
            echo "  ⚠ Port $PORT is used by process: $PROCESS (PID: $PID)"
        else
            echo "  ✓ Port $PORT is available"
        fi
    fi
    echo ""
done

echo "=== Docker Containers Using These Ports ==="
docker ps --format "table {{.Names}}\t{{.Ports}}" 2>/dev/null | grep -E "2181|9092|5432|6379|NAME" || echo "No Docker containers found"

echo ""
echo "=== Recommendations ==="
echo "1. If containers are from FakeApiDemo, stop them:"
echo "   docker stop fakeapidemo-postgres fakeapidemo-kafka fakeapidemo-zookeeper"
echo ""
echo "2. Or use different ports in docker-compose.yml (see PORT_CONFLICT_FIX.md)"
echo ""
echo "3. Then start audiobook services:"
echo "   docker-compose up -d postgres redis zookeeper kafka"
