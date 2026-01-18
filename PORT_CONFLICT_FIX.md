# Fix Port Conflicts - Use Existing Services

## Problem
Ports are already in use:
- **PostgreSQL**: 5432
- **Redis**: 6379
- **Zookeeper**: 2181
- **Kafka**: 9092

## Solution Options

### Option 1: Stop Existing Containers (Recommended if they're from old projects)

**Check what's running:**
```bash
docker ps
```

**Stop specific containers:**
```bash
# Stop containers by name
docker stop $(docker ps -q --filter "name=kafka")
docker stop $(docker ps -q --filter "name=zookeeper")
docker stop $(docker ps -q --filter "name=postgres")

# Or stop all Docker containers
docker stop $(docker ps -q)
```

**Then start audiobook services:**
```bash
cd audiobook-service
docker-compose up -d postgres redis zookeeper kafka
```

---

### Option 2: Use Existing Services (If they're already configured)

If your existing PostgreSQL, Kafka, and Redis are already set up and working, you can:

**1. Update `application.yml` to use existing services:**
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/audiobooks  # Use existing PostgreSQL
  data:
    redis:
      host: localhost
      port: 6379  # Use existing Redis
  kafka:
    bootstrap-servers: localhost:9092  # Use existing Kafka
```

**2. Start only the API service:**
```bash
cd audiobook-service
# Comment out services you're already using in docker-compose.yml
# Or just build and run the API separately
docker-compose up -d api
```

---

### Option 3: Change Ports (Use Different Ports)

If you want to keep both sets of services running, modify `docker-compose.yml` to use different ports.

Update the ports section:
```yaml
postgres:
  ports:
    - "5433:5432"  # Changed from 5432 to 5433

redis:
  ports:
    - "6380:6379"  # Changed from 6379 to 6380

zookeeper:
  ports:
    - "2182:2181"  # Changed from 2181 to 2182

kafka:
  ports:
    - "9094:9092"  # Changed from 9092 to 9094
    - "9095:9093"
```

Then update `application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5433/audiobooks
  data:
    redis:
      host: localhost
      port: 6380
  kafka:
    bootstrap-servers: localhost:9094
```

---

## Quick Fix Commands

### Check What's Using Ports:
```bash
# Check Docker containers
docker ps | grep -E "kafka|zookeeper|postgres|redis"

# Check ports directly
lsof -i :2181  # Zookeeper
lsof -i :9092  # Kafka
lsof -i :5432  # PostgreSQL
lsof -i :6379  # Redis
```

### Stop Specific Containers:
```bash
# Find containers by name pattern
docker ps --filter "name=kafka" --format "{{.ID}}" | xargs docker stop
docker ps --filter "name=zookeeper" --format "{{.ID}}" | xargs docker stop
docker ps --filter "name=postgres" --format "{{.ID}}" | xargs docker stop
docker ps --filter "name=redis" --format "{{.ID}}" | xargs docker stop
```

### Or Stop All and Start Fresh:
```bash
# Stop all containers
docker stop $(docker ps -q)

# Start audiobook services
cd audiobook-service
docker-compose up -d postgres redis zookeeper kafka
```

---

## Recommended Approach

**If the existing services are from the FakeApiDemo project:**

1. Stop the old containers:
   ```bash
   docker stop fakeapidemo-postgres fakeapidemo-kafka fakeapidemo-zookeeper
   ```

2. Start audiobook services:
   ```bash
   cd audiobook-service
   docker-compose up -d postgres redis zookeeper kafka
   ```

**If you want to keep both projects separate:**

Use Option 3 (different ports) so both can run simultaneously.

---

## Verify Services Are Running

After starting services:
```bash
# Check Docker containers
docker ps

# Verify ports
docker-compose ps

# Test connections
docker exec -it audiobook-postgres psql -U postgres -d audiobooks -c "SELECT 1;"
docker exec -it audiobook-redis redis-cli ping
docker exec -it audiobook-kafka kafka-topics --bootstrap-server localhost:9092 --list
```
