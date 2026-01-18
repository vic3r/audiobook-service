#!/bin/bash

# Java Setup Script for Audiobook Service
# This script helps install and configure Java 21 or 17

echo "=== Java Setup for Audiobook Service ==="
echo ""

# Check if Java 21 is installed
if /usr/libexec/java_home -v 21 &>/dev/null; then
    echo "✓ Java 21 is already installed"
    JAVA_21_HOME=$(/usr/libexec/java_home -v 21)
    echo "  Location: $JAVA_21_HOME"
    echo ""
    echo "To use Java 21, run:"
    echo "  export JAVA_HOME=\$(/usr/libexec/java_home -v 21)"
    echo "  cd audiobook-service && mvn clean compile"
    exit 0
fi

# Check if Java 17 is installed
if /usr/libexec/java_home -v 17 &>/dev/null; then
    echo "✓ Java 17 is already installed"
    JAVA_17_HOME=$(/usr/libexec/java_home -v 17)
    echo "  Location: $JAVA_17_HOME"
    echo ""
    echo "To use Java 17, run:"
    echo "  export JAVA_HOME=\$(/usr/libexec/java_home -v 17)"
    echo "  cd audiobook-service && mvn clean compile"
    exit 0
fi

echo "⚠ Java 17 or 21 is not installed"
echo ""
echo "Installing Java 21 (LTS)..."
echo ""

# Check if Homebrew is installed
if ! command -v brew &> /dev/null; then
    echo "❌ Homebrew is not installed."
    echo "Please install Homebrew first: https://brew.sh"
    exit 1
fi

# Install OpenJDK 21 (JDK includes compiler)
echo "Running: brew install openjdk@21"
brew install openjdk@21

if [ $? -eq 0 ]; then
    echo ""
    echo "✓ OpenJDK 21 installed successfully!"
    echo ""
    echo "Setting up JAVA_HOME..."
    export JAVA_HOME=$(/usr/libexec/java_home -v 21 2>/dev/null)
    
    if [ -z "$JAVA_HOME" ]; then
        # If java_home doesn't find it, use the Homebrew path
        JAVA_HOME="/opt/homebrew/opt/openjdk@21"
    fi
    
    export JAVA_HOME
    export PATH="$JAVA_HOME/bin:$PATH"
    
    echo "JAVA_HOME=$JAVA_HOME"
    echo ""
    
    # Verify javac exists
    if command -v javac &> /dev/null; then
        echo "✓ JDK (Java compiler) is available:"
        javac -version
        echo ""
        echo "To use Java 21 for this session, run:"
        echo "  export JAVA_HOME=\$(/usr/libexec/java_home -v 21)"
        echo "  export PATH=\"\$JAVA_HOME/bin:\$PATH\""
        echo "  cd audiobook-service && mvn clean compile"
        echo ""
        echo "Or add to your ~/.zshrc for permanent setup:"
        echo "  echo 'export JAVA_HOME=\$(/usr/libexec/java_home -v 21)' >> ~/.zshrc"
        echo "  echo 'export PATH=\"\$JAVA_HOME/bin:\$PATH\"' >> ~/.zshrc"
        echo "  source ~/.zshrc"
    else
        echo "⚠ javac not found. Please check installation."
        echo "Try: export PATH=\"/opt/homebrew/opt/openjdk@21/bin:\$PATH\""
    fi
else
    echo ""
    echo "❌ Installation failed. Please install OpenJDK 21 manually:"
    echo "  brew install openjdk@21"
    exit 1
fi
