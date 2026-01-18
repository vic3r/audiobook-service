# Java Version Compatibility Issue - Fix Guide

## Problem
You're using **Java 25**, but Spring Boot 3.2.0 and Lombok have compatibility issues with Java 25.

Error: `java.lang.ExceptionInInitializerError: com.sun.tools.javac.code.TypeTag :: UNKNOWN`

## Solution: Switch to Java 21 (LTS) or Java 17

### Option 1: Install and Use Java 21 (Recommended)

**Using Homebrew:**
```bash
# Install Java 21
brew install openjdk@21

# Set JAVA_HOME for this session
export JAVA_HOME=$(/usr/libexec/java_home -v 21)

# Or add to your ~/.zshrc for permanent setup
echo 'export JAVA_HOME=$(/usr/libexec/java_home -v 21)' >> ~/.zshrc
source ~/.zshrc

# Verify Java version
java -version  # Should show 21.x.x
mvn -version   # Should show Java 21
```

**Using SDKMAN (Alternative):**
```bash
# Install SDKMAN if not already installed
curl -s "https://get.sdkman.io" | bash
source "$HOME/.sdkman/bin/sdkman-init.sh"

# Install Java 21
sdk install java 21-open

# Use Java 21
sdk use java 21-open
```

### Option 2: Install and Use Java 17

**Using Homebrew:**
```bash
brew install openjdk@17
export JAVA_HOME=$(/usr/libexec/java_home -v 17)
```

**Using SDKMAN:**
```bash
sdk install java 17.0.9-tem
sdk use java 17.0.9-tem
```

### Option 3: Use jenv to Manage Multiple Java Versions

```bash
# Install jenv
brew install jenv

# Add to ~/.zshrc
echo 'export PATH="$HOME/.jenv/bin:$PATH"' >> ~/.zshrc
echo 'eval "$(jenv init -)"' >> ~/.zshrc
source ~/.zshrc

# Add Java installations to jenv
jenv add /opt/homebrew/opt/openjdk@21/libexec/openjdk.jdk/Contents/Home
jenv add /opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home

# Set local Java version for this project
cd audiobook-service
jenv local 21  # or 17

# Verify
java -version
```

## After Switching Java Version

1. **Clean and compile:**
   ```bash
   cd audiobook-service
   mvn clean compile
   ```

2. **If errors persist, clear Maven cache:**
   ```bash
   rm -rf ~/.m2/repository/org/projectlombok
   mvn clean compile
   ```

## Why Java 21?

- **LTS (Long Term Support)** - Supported until September 2029
- **Fully compatible** with Spring Boot 3.2.0
- **Stable** - No compatibility issues with Lombok or Maven plugins
- **Modern features** - Latest stable features before Java 22+

## Current Java Version Check

To check which Java versions you have installed:
```bash
/usr/libexec/java_home -V
```

To verify current Java version:
```bash
java -version
mvn -version
```

## Project Configuration

The project is configured for Java 17 in `pom.xml`, which works with Java 17, 21, or newer versions (up to 21). Java 25 has breaking changes that cause this error.
