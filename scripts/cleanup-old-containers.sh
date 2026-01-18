#!/bin/bash

# Script to stop and remove old FakeApiDemo containers that might be interfering

echo "Stopping and removing old FakeApiDemo containers..."

# Stop FakeApiDemo containers
docker stop fakeapidemo-api fakeapidemo-postgres fakeapidemo-zookeeper fakeapidemo-kafka 2>/dev/null

# Remove FakeApiDemo containers
docker rm fakeapidemo-api fakeapidemo-postgres fakeapidemo-zookeeper fakeapidemo-kafka 2>/dev/null

echo "Checking for any containers trying to connect to BillingTransactions..."
docker ps -a | grep -i billing || echo "No BillingTransactions-related containers found"

echo "Done! You can now start the audiobook services with: docker-compose up -d"
