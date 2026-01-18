#!/bin/bash

# Quick script to use Java 21 for this terminal session

JAVA_21_HOME="/opt/homebrew/opt/openjdk@21"

if [ ! -f "$JAVA_21_HOME/bin/javac" ]; then
    echo "❌ OpenJDK 21 not found at $JAVA_21_HOME"
    echo "Please install it first: brew install openjdk@21"
    exit 1
fi

export JAVA_HOME="$JAVA_21_HOME"
export PATH="$JAVA_21_HOME/bin:$PATH"

echo "✓ Java 21 configured!"
echo "JAVA_HOME=$JAVA_HOME"
echo ""
echo "Verifying installation:"
java -version
echo ""
javac -version
echo ""
echo "Maven will use:"
mvn -version | grep "Java version" || mvn -version | head -3
echo ""
echo "You can now compile:"
echo "  mvn clean compile"
