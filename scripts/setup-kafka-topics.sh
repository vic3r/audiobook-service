#!/bin/bash

# Setup Kafka Topics for Audiobook Service
# This script creates the required Kafka topics

set -e

KAFKA_CONTAINER="audiobook-kafka"
BOOTSTRAP_SERVER="localhost:9092"

# Colors for output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo -e "${YELLOW}Setting up Kafka topics...${NC}"

# Check if Kafka container is running
if ! docker ps | grep -q "$KAFKA_CONTAINER"; then
    echo -e "${RED}Error: Kafka container '$KAFKA_CONTAINER' is not running${NC}"
    echo "Please start Kafka first: docker-compose up -d kafka"
    exit 1
fi

# Wait for Kafka to be ready
echo -e "${YELLOW}Waiting for Kafka to be ready...${NC}"
sleep 5

# Function to create topic if it doesn't exist
create_topic() {
    local topic_name=$1
    local partitions=${2:-3}
    local replication_factor=${3:-1}
    
    echo -e "${YELLOW}Creating topic: $topic_name${NC}"
    
    docker exec -it $KAFKA_CONTAINER kafka-topics \
        --bootstrap-server localhost:9092 \
        --create \
        --if-not-exists \
        --topic "$topic_name" \
        --partitions "$partitions" \
        --replication-factor "$replication_factor" \
        --config min.insync.replicas=1
    
    if [ $? -eq 0 ]; then
        echo -e "${GREEN}✓ Topic '$topic_name' created successfully${NC}"
    else
        echo -e "${RED}✗ Failed to create topic '$topic_name'${NC}"
        return 1
    fi
}

# Create topics
create_topic "library-events" 3 1
create_topic "purchase-events" 3 1

# List all topics
echo -e "\n${YELLOW}Listing all topics:${NC}"
docker exec -it $KAFKA_CONTAINER kafka-topics \
    --bootstrap-server localhost:9092 \
    --list

echo -e "\n${GREEN}Kafka topics setup completed!${NC}"
echo -e "${YELLOW}Topics created:${NC}"
echo "  - library-events (3 partitions, 1 replica)"
echo "  - purchase-events (3 partitions, 1 replica)"
