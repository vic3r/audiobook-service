# Fix: "No compiler is provided" Error

## Problem
Error: `No compiler is provided in this environment. Perhaps you are running on a JRE rather than a JDK?`

This means you have a **JRE** (Java Runtime Environment) but need a **JDK** (Java Development Kit) which includes the compiler (`javac`).

## Solution: Install JDK 21

### Option 1: Using Homebrew (Recommended)
```bash
# Install OpenJDK 21 (includes JDK)
brew install openjdk@21

# Set JAVA_HOME to point to the JDK
export JAVA_HOME=$(/usr/libexec/java_home -v 21)

# Verify JDK is installed (should show javac)
javac -version  # Should show "javac 21.x.x"

# Verify JAVA_HOME points to JDK (should contain "jdk" or "openjdk")
echo $JAVA_HOME

# Now compile
mvn clean compile
```

### Option 2: Add to PATH (if OpenJDK is installed)
If you already installed `openjdk@21` but it's not in your PATH:

```bash
# Add OpenJDK 21 to PATH
export PATH="/opt/homebrew/opt/openjdk@21/bin:$PATH"

# Set JAVA_HOME
export JAVA_HOME="/opt/homebrew/opt/openjdk@21"

# Verify
javac -version
mvn -version  # Should show Java 21

# Compile
mvn clean compile
```

### Option 3: Link OpenJDK (if installed but not linked)
```bash
# Install OpenJDK 21
brew install openjdk@21

# Link it (creates symlinks)
sudo ln -sfn /opt/homebrew/opt/openjdk@21/libexec/openjdk.jdk /Library/Java/JavaVirtualMachines/openjdk-21.jdk

# Set JAVA_HOME
export JAVA_HOME=$(/usr/libexec/java_home -v 21)

# Verify
javac -version
mvn clean compile
```

## Verify Installation

Check these commands:
```bash
# Should show Java 21
java -version

# Should show javac 21.x.x (this is the compiler)
javac -version

# Should show Java 21 in JAVA_HOME
echo $JAVA_HOME

# Maven should use Java 21
mvn -version
```

All should show **Java 21** (or at least Java 17+).

## Permanent Setup

Add to your `~/.zshrc`:
```bash
echo 'export JAVA_HOME=$(/usr/libexec/java_home -v 21)' >> ~/.zshrc
echo 'export PATH="$JAVA_HOME/bin:$PATH"' >> ~/.zshrc
source ~/.zshrc
```

## Quick Check: What's Currently Installed?

```bash
# Check available Java versions
/usr/libexec/java_home -V

# Check if javac exists
which javac

# Check current Java version Maven uses
mvn -version | grep "Java version"
```

## After Fixing

Once `javac -version` works, try:
```bash
cd audiobook-service
mvn clean compile
```

This should now work! 🎉
