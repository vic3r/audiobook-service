#!/bin/bash

# Install JDK 21 and configure it for Maven

echo "=== Installing OpenJDK 21 (JDK with compiler) ==="
echo ""

# Check if Homebrew is installed
if ! command -v brew &> /dev/null; then
    echo "❌ Homebrew is not installed."
    echo "Please install Homebrew first: https://brew.sh"
    exit 1
fi

# Install OpenJDK 21
echo "Installing OpenJDK 21..."
brew install openjdk@21

if [ $? -ne 0 ]; then
    echo "❌ Installation failed. Please check your Homebrew setup."
    exit 1
fi

echo ""
echo "✓ OpenJDK 21 installed!"
echo ""

# Use Homebrew path directly (this is the correct location)
JAVA_21_HOME="/opt/homebrew/opt/openjdk@21"

# Verify javac exists at this location
if [ ! -f "$JAVA_21_HOME/bin/javac" ]; then
    echo "⚠ Warning: javac not found at $JAVA_21_HOME/bin/javac"
    echo "Checking alternative locations..."
    
    # Try to find via java_home
    JAVA_21_HOME=$(/usr/libexec/java_home -v 21 2>/dev/null)
    
    if [ -z "$JAVA_21_HOME" ] || [ ! -f "$JAVA_21_HOME/bin/javac" ]; then
        # Last resort: check common Homebrew locations
        if [ -f "/opt/homebrew/opt/openjdk@21/bin/javac" ]; then
            JAVA_21_HOME="/opt/homebrew/opt/openjdk@21"
        elif [ -f "/usr/local/opt/openjdk@21/bin/javac" ]; then
            JAVA_21_HOME="/usr/local/opt/openjdk@21"
        else
            echo "❌ Could not find OpenJDK 21 javac compiler"
            echo "Please verify installation: brew list openjdk@21"
            exit 1
        fi
    fi
fi

echo "Java 21 location: $JAVA_21_HOME"
echo ""

# Set JAVA_HOME
export JAVA_HOME="$JAVA_21_HOME"
export PATH="$JAVA_HOME/bin:$PATH"

echo "Setting up environment..."
echo ""

# Verify javac
if [ -f "$JAVA_HOME/bin/javac" ]; then
    echo "✓ JDK compiler found!"
    "$JAVA_HOME/bin/javac" -version
    echo ""
    echo "Current Java version:"
    "$JAVA_HOME/bin/java" -version
    echo ""
    echo "Maven will use:"
    mvn -version | grep "Java version" || echo "Run 'mvn -version' to check"
    echo ""
    echo "=== Setup Complete! ==="
    echo ""
    echo "To use Java 21 in this terminal session, run:"
    echo "  export JAVA_HOME=\"$JAVA_21_HOME\""
    echo "  export PATH=\"\$JAVA_HOME/bin:\$PATH\""
    echo ""
    echo "Or add to ~/.zshrc for permanent setup:"
    echo "  echo 'export JAVA_HOME=\"$JAVA_21_HOME\"' >> ~/.zshrc"
    echo "  echo 'export PATH=\"\$JAVA_HOME/bin:\$PATH\"' >> ~/.zshrc"
    echo "  source ~/.zshrc"
    echo ""
    echo "Then compile:"
    echo "  cd audiobook-service"
    echo "  mvn clean compile"
else
    echo "⚠ javac not found at $JAVA_HOME/bin/javac"
    echo "Please check the installation manually."
    exit 1
fi
