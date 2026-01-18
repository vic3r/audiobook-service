# Install JDK 21 - Step by Step

## Problem
You need a **JDK** (Java Development Kit) which includes the compiler (`javac`), not just a JRE (Java Runtime Environment).

## Solution: Install OpenJDK 21

### Quick Install (Recommended)

Run the automated script:
```bash
cd audiobook-service
./install-jdk.sh
```

This will:
1. Install OpenJDK 21 via Homebrew
2. Set up JAVA_HOME
3. Verify the installation

### Manual Install

If you prefer to do it manually:

```bash
# 1. Install OpenJDK 21
brew install openjdk@21

# 2. Set JAVA_HOME (try this first)
export JAVA_HOME=$(/usr/libexec/java_home -v 21)

# If that doesn't work, use the direct path:
export JAVA_HOME="/opt/homebrew/opt/openjdk@21"

# 3. Add JDK to PATH
export PATH="$JAVA_HOME/bin:$PATH"

# 4. Verify installation
javac -version  # Should show "javac 21.x.x"
java -version    # Should show "openjdk version 21.x.x"
mvn -version     # Should show "Java version: 21.x.x"

# 5. Compile the project
cd audiobook-service
mvn clean compile
```

## Permanent Setup

Add to your `~/.zshrc` so it works every time:

```bash
# Add these lines to ~/.zshrc
echo 'export JAVA_HOME="/opt/homebrew/opt/openjdk@21"' >> ~/.zshrc
echo 'export PATH="$JAVA_HOME/bin:$PATH"' >> ~/.zshrc

# Reload your shell
source ~/.zshrc
```

## Verify Everything Works

After installation, verify:

```bash
# Check Java version
java -version
# Should show: openjdk version "21.x.x"

# Check compiler
javac -version
# Should show: javac 21.x.x

# Check Maven uses Java 21
mvn -version
# Should show: Java version: 21.x.x (NOT 25!)

# Compile project
cd audiobook-service
mvn clean compile
# Should succeed! ✅
```

## Troubleshooting

### If `javac` still not found after installation:

```bash
# Check if OpenJDK 21 is installed
ls -la /opt/homebrew/opt/openjdk@21/bin/javac

# If it exists, add to PATH explicitly
export PATH="/opt/homebrew/opt/openjdk@21/bin:$PATH"
javac -version
```

### If Maven still uses Java 25:

```bash
# Explicitly set JAVA_HOME before running Maven
export JAVA_HOME="/opt/homebrew/opt/openjdk@21"
export PATH="$JAVA_HOME/bin:$PATH"
mvn -version  # Should now show Java 21
```

### Check what Java versions are available:

```bash
/usr/libexec/java_home -V
```

You should see Java 21 in the list.

## Why OpenJDK 21?

- **LTS (Long Term Support)** - Supported until 2029
- **Includes JDK** - Has `javac` compiler
- **Compatible** - Works with Spring Boot 3.2.0 and Lombok
- **Stable** - No compatibility issues

## After Successful Installation

Once `mvn clean compile` succeeds, you're all set! The project will compile and you can run:

```bash
mvn spring-boot:run
```

Or build the JAR:
```bash
mvn clean package
```
