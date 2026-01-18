# Quick Fix: Java Version Issue

## Problem
Your Java version output is confusing (`1.8.0_471` but JVM build `25.471-b09`), and Maven compilation fails due to Java 25 compatibility issues.

## Quick Solution (3 Steps)

### Step 1: Install Java 21
```bash
brew install openjdk@21
```

### Step 2: Set JAVA_HOME for this terminal session
```bash
export JAVA_HOME=$(/usr/libexec/java_home -v 21)
```

### Step 3: Verify and compile
```bash
java -version  # Should show Java 21.x.x (not 1.8 or 25)
mvn -version   # Should show Java 21.x.x
cd audiobook-service
mvn clean compile
```

## Or Use the Setup Script
```bash
chmod +x setup-java.sh
./setup-java.sh
```

## Permanent Setup (Optional)
Add to `~/.zshrc` to always use Java 21:
```bash
echo 'export JAVA_HOME=$(/usr/libexec/java_home -v 21)' >> ~/.zshrc
source ~/.zshrc
```

## Why This Works
- Java 21 is LTS (Long Term Support) until 2029
- Fully compatible with Spring Boot 3.2.0 and Lombok
- Stable and widely supported

## Verify Installation
After installing, check available Java versions:
```bash
/usr/libexec/java_home -V
```

You should see Java 21 in the list.
