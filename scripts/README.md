# Setup Scripts for Audiobook Service

This directory contains scripts to set up the database and Kafka topics for the Audiobook Service.

## Scripts Overview

### Database Setup
- **setup-database.sh** / **setup-database.ps1** - Creates the PostgreSQL database
- **setup-database.sql** - SQL script for manual database creation (optional)

### Kafka Topics Setup
- **setup-kafka-topics.sh** / **setup-kafka-topics.ps1** - Creates required Kafka topics

### Complete Setup
- **setup-all.sh** / **setup-all.ps1** - Sets up both database and Kafka topics

## Quick Start

### Option 1: Complete Setup (Recommended)

**Linux/macOS:**
```bash
cd audiobook-service
./scripts/setup-all.sh
```

**Windows (PowerShell):**
```powershell
cd audiobook-service
.\scripts\setup-all.ps1
```

This will:
1. Start all infrastructure services (PostgreSQL, Redis, Zookeeper, Kafka)
2. Create the database
3. Create Kafka topics

### Option 2: Manual Setup

#### 1. Start Infrastructure Services

```bash
cd audiobook-service
docker-compose up -d postgres redis zookeeper kafka
```

#### 2. Setup Database

**Linux/macOS:**
```bash
./scripts/setup-database.sh
```

**Windows (PowerShell):**
```powershell
.\scripts\setup-database.ps1
```

**Note:** The database `audiobooks` is automatically created by Docker Compose. This script verifies it exists. Tables are created automatically by Spring Boot JPA when the application starts (ddl-auto: update).

#### 3. Setup Kafka Topics

**Linux/macOS:**
```bash
./scripts/setup-kafka-topics.sh
```

**Windows (PowerShell):**
```powershell
.\scripts\setup-kafka-topics.ps1
```

Creates the following topics:
- `library-events` (3 partitions, 1 replica)
- `purchase-events` (3 partitions, 1 replica)

## Individual Scripts

### Database Setup Scripts

#### setup-database.sh / setup-database.ps1

**Purpose:** Creates the PostgreSQL database `audiobooks`

**Usage:**
```bash
./scripts/setup-database.sh
```

**What it does:**
- Checks if PostgreSQL container is running
- Creates database `audiobooks` if it doesn't exist
- Lists available databases

**Note:** Tables are automatically created by Spring Boot when the application starts (no manual SQL needed).

---

### Kafka Topics Setup Scripts

#### setup-kafka-topics.sh / setup-kafka-topics.ps1

**Purpose:** Creates required Kafka topics

**Usage:**
```bash
./scripts/setup-kafka-topics.sh
```

**What it does:**
- Checks if Kafka container is running
- Creates topics: `library-events`, `purchase-events`
- Lists all available topics

**Topics Created:**
- `library-events` - For library-related events (ADDED, POSITION_UPDATED, FAVORITE_TOGGLED)
- `purchase-events` - For purchase-related events

---

## Manual Setup (Alternative)

### Database (SQL)

If you need to create the database manually:

```bash
docker exec -it audiobook-postgres psql -U postgres -c "CREATE DATABASE audiobooks;"
```

Or use the SQL script:
```bash
docker exec -i audiobook-postgres psql -U postgres < scripts/setup-database.sql
```

### Kafka Topics (Manual)

Create topics manually:

```bash
# Library events topic
docker exec -it audiobook-kafka kafka-topics \
  --bootstrap-server localhost:9092 \
  --create \
  --topic library-events \
  --partitions 3 \
  --replication-factor 1

# Purchase events topic
docker exec -it audiobook-kafka kafka-topics \
  --bootstrap-server localhost:9092 \
  --create \
  --topic purchase-events \
  --partitions 3 \
  --replication-factor 1
```

## Verification

### Verify Database

```bash
# List databases
docker exec -it audiobook-postgres psql -U postgres -c "\l"

# Connect to database
docker exec -it audiobook-postgres psql -U postgres -d audiobooks

# List tables (after Spring Boot starts)
docker exec -it audiobook-postgres psql -U postgres -d audiobooks -c "\dt"
```

### Verify Kafka Topics

```bash
# List all topics
docker exec -it audiobook-kafka kafka-topics \
  --bootstrap-server localhost:9092 \
  --list

# Describe a topic
docker exec -it audiobook-kafka kafka-topics \
  --bootstrap-server localhost:9092 \
  --describe \
  --topic library-events
```

### Test Kafka Consumer

Listen to events:
```bash
docker exec -it audiobook-kafka kafka-console-consumer \
  --bootstrap-server localhost:9092 \
  --topic library-events \
  --from-beginning
```

## Troubleshooting

### Database Script Fails

**Error:** "PostgreSQL container is not running"
- **Solution:** Start PostgreSQL: `docker-compose up -d postgres`
- Wait a few seconds for it to be ready

**Error:** "Database already exists"
- **Solution:** This is normal - the database already exists. Continue with next steps.

### Kafka Script Fails

**Error:** "Kafka container is not running"
- **Solution:** Start Kafka and Zookeeper: `docker-compose up -d zookeeper kafka`
- Wait 10-15 seconds for Kafka to fully start

**Error:** "Topic already exists"
- **Solution:** This is normal - the topics already exist. Continue with next steps.

### Container Names

If you're using custom container names, edit the scripts to match your container names:
- Default PostgreSQL container: `audiobook-postgres`
- Default Kafka container: `audiobook-kafka`

## Prerequisites

- Docker and Docker Compose installed
- Containers running:
  - `audiobook-postgres` (PostgreSQL)
  - `audiobook-kafka` (Kafka)
  - `audiobook-zookeeper` (Zookeeper)

## Next Steps

After running the setup scripts:

1. **Start Spring Boot application:**
   ```bash
   mvn spring-boot:run
   ```
   Or with Docker:
   ```bash
   docker-compose up -d --build api
   ```

2. **Verify setup:**
   - Check database tables: Tables are created automatically
   - Check Kafka topics: Topics should be listed
   - Access API: http://localhost:8080/swagger-ui.html

3. **Test the system:**
   - Create a user account
   - Add audiobooks to library
   - Monitor Kafka events

## Notes

- **Database:** The database `audiobooks` is created automatically by Docker Compose. Tables are created by Spring Boot JPA automatically (ddl-auto: update).

- **Kafka Topics:** Topics can also be created automatically by Spring Boot (KafkaConfig.java), but the scripts ensure they exist before the application starts.

- **Idempotency:** All scripts are idempotent - safe to run multiple times.
