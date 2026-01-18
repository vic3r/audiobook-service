#!/bin/bash

# Complete Setup Script for Audiobook Service
# This script sets up both database and Kafka topics

set -e

# Colors for output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
PROJECT_DIR="$( cd "$SCRIPT_DIR/.." && pwd )"

echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}  Audiobook Service Setup${NC}"
echo -e "${BLUE}========================================${NC}\n"

# Check if Docker is running
if ! docker ps > /dev/null 2>&1; then
    echo -e "${RED}Error: Docker is not running${NC}"
    exit 1
fi

# Start infrastructure services
echo -e "${YELLOW}Starting infrastructure services...${NC}"
cd "$PROJECT_DIR"
docker-compose up -d postgres redis zookeeper kafka

# Wait for services to be ready
echo -e "${YELLOW}Waiting for services to be ready...${NC}"
sleep 10

# Setup database
echo -e "\n${BLUE}--- Setting up Database ---${NC}"
bash "$SCRIPT_DIR/setup-database.sh"

# Setup Kafka topics
echo -e "\n${BLUE}--- Setting up Kafka Topics ---${NC}"
bash "$SCRIPT_DIR/setup-kafka-topics.sh"

echo -e "\n${GREEN}========================================${NC}"
echo -e "${GREEN}  Setup Complete!${NC}"
echo -e "${GREEN}========================================${NC}\n"

echo -e "${YELLOW}Next steps:${NC}"
echo "1. Start the Spring Boot application:"
echo "   cd $PROJECT_DIR"
echo "   mvn spring-boot:run"
echo ""
echo "2. Or build and run with Docker:"
echo "   docker-compose up -d --build api"
echo ""
echo "3. Access the API:"
echo "   - API: http://localhost:8080"
echo "   - Swagger UI: http://localhost:8080/swagger-ui.html"
