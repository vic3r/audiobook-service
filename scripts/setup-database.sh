#!/bin/bash

# Setup Database for Audiobook Service
# This script creates the PostgreSQL database

set -e

POSTGRES_CONTAINER="audiobook-postgres"
DATABASE_NAME="audiobooks"
POSTGRES_USER="postgres"
POSTGRES_PASSWORD="postgres"

# Colors for output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo -e "${YELLOW}Setting up database...${NC}"

# Check if PostgreSQL container is running
if ! docker ps | grep -q "$POSTGRES_CONTAINER"; then
    echo -e "${RED}Error: PostgreSQL container '$POSTGRES_CONTAINER' is not running${NC}"
    echo "Please start PostgreSQL first: docker-compose up -d postgres"
    exit 1
fi

# Wait for PostgreSQL to be ready
echo -e "${YELLOW}Waiting for PostgreSQL to be ready...${NC}"
sleep 3

# Check if database already exists
DB_EXISTS=$(docker exec -it $POSTGRES_CONTAINER psql -U $POSTGRES_USER -tAc "SELECT 1 FROM pg_database WHERE datname='$DATABASE_NAME'")

if [ "$DB_EXISTS" = "1" ]; then
    echo -e "${YELLOW}Database '$DATABASE_NAME' already exists${NC}"
else
    echo -e "${YELLOW}Creating database '$DATABASE_NAME'...${NC}"
    docker exec -it $POSTGRES_CONTAINER psql -U $POSTGRES_USER -c "CREATE DATABASE $DATABASE_NAME;" 2>&1
    
    EXIT_CODE=$?
    if [ $EXIT_CODE -eq 0 ]; then
        echo -e "${GREEN}✓ Database '$DATABASE_NAME' created successfully${NC}"
    elif docker exec -it $POSTGRES_CONTAINER psql -U $POSTGRES_USER -tAc "SELECT 1 FROM pg_database WHERE datname='$DATABASE_NAME'" | grep -q 1; then
        # Database exists (might have been created between check and create)
        echo -e "${YELLOW}Database '$DATABASE_NAME' already exists (created concurrently)${NC}"
    else
        echo -e "${RED}✗ Failed to create database '$DATABASE_NAME'${NC}"
        exit 1
    fi
fi

# Note: Tables are created automatically by Spring Boot JPA (ddl-auto: update)
echo -e "${YELLOW}Note: Tables will be created automatically when Spring Boot starts${NC}"
echo -e "${YELLOW}Spring Boot uses ddl-auto: update to create/update tables${NC}"

echo -e "\n${GREEN}Database setup completed!${NC}"
echo -e "${YELLOW}Database: $DATABASE_NAME${NC}"
echo -e "${YELLOW}Connection: Host=localhost, Port=5432, Database=$DATABASE_NAME, User=$POSTGRES_USER${NC}"

# List databases
echo -e "\n${YELLOW}Available databases:${NC}"
docker exec -it $POSTGRES_CONTAINER psql -U $POSTGRES_USER -c "\l" | grep -E "Name|$DATABASE_NAME"
