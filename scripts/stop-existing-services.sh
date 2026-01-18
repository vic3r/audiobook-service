#!/bin/bash

# Stop Existing Services Script
# This script stops any existing containers/services that might conflict

set -e

echo "=== Stopping Existing Services ==="
echo ""

# Check for Docker containers
echo "Checking for existing Docker containers..."
CONTAINERS=$(docker ps --format "{{.Names}}" | grep -E "kafka|zookeeper|postgres|redis|audiobook" || true)

if [ -z "$CONTAINERS" ]; then
    echo "✓ No conflicting Docker containers found"
else
    echo "Found containers:"
    echo "$CONTAINERS"
    echo ""
    read -p "Stop these containers? (y/n) " -n 1 -r
    echo ""
    if [[ $REPLY =~ ^[Yy]$ ]]; then
        echo "Stopping containers..."
        echo "$CONTAINERS" | xargs docker stop
        echo "✓ Containers stopped"
    else
        echo "Skipped stopping containers"
    fi
fi

echo ""
echo "Checking for processes on ports..."
PORTS="2181 9092 5432 6379"

for PORT in $PORTS; do
    PID=$(lsof -ti :$PORT 2>/dev/null || true)
    if [ ! -z "$PID" ]; then
        echo "⚠ Port $PORT is in use by PID $PID"
        read -p "Kill process on port $PORT? (y/n) " -n 1 -r
        echo ""
        if [[ $REPLY =~ ^[Yy]$ ]]; then
            kill -9 $PID 2>/dev/null || true
            echo "✓ Port $PORT freed"
        fi
    else
        echo "✓ Port $PORT is available"
    fi
done

echo ""
echo "=== Done ==="
