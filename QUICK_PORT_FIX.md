# Quick Fix: Port Conflicts

## Problem
Ports 2181 (Zookeeper), 9092 (Kafka), and 5432 (PostgreSQL) are already in use by **FakeApiDemo** containers.

## Solution: Stop FakeApiDemo Containers

### Quick Fix (One Command)

```bash
# Stop FakeApiDemo containers
docker stop fakeapidemo-zookeeper fakeapidemo-kafka fakeapidemo-postgres
```

### Or Use the Script

```bash
cd audiobook-service
./scripts/stop-fakeapidemo.sh
```

### Then Start Audiobook Services

```bash
cd audiobook-service
docker-compose up -d postgres redis zookeeper kafka
```

---

## Verify Ports Are Free

```bash
# Check if ports are available
./scripts/check-ports.sh
```

All ports should show as "available" after stopping FakeApiDemo containers.

---

## Alternative: Use Different Ports

If you want to keep both projects running simultaneously, see `PORT_CONFLICT_FIX.md` for instructions on changing ports.

---

## Quick Commands

```bash
# 1. Stop FakeApiDemo containers
docker stop fakeapidemo-zookeeper fakeapidemo-kafka fakeapidemo-postgres

# 2. Start Audiobook services
cd audiobook-service
docker-compose up -d postgres redis zookeeper kafka

# 3. Verify services are running
docker-compose ps

# 4. Setup database and Kafka topics
./scripts/setup-database.sh
./scripts/setup-kafka-topics.sh
```

Done! 🎉
