#!/bin/bash

# Stop FakeApiDemo containers to free up ports for Audiobook Service

echo "=== Stopping FakeApiDemo Containers ==="
echo ""

CONTAINERS=("fakeapidemo-zookeeper" "fakeapidemo-kafka" "fakeapidemo-postgres")

for CONTAINER in "${CONTAINERS[@]}"; do
    if docker ps --format "{{.Names}}" | grep -q "^${CONTAINER}$"; then
        echo "Stopping $CONTAINER..."
        docker stop "$CONTAINER"
        echo "✓ Stopped $CONTAINER"
    else
        echo "✓ $CONTAINER is not running"
    fi
done

echo ""
echo "=== Containers Stopped ==="
echo ""
echo "Now you can start Audiobook services:"
echo "  cd audiobook-service"
echo "  docker-compose up -d postgres redis zookeeper kafka"
