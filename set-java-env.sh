#!/bin/bash

# Set Java 21 environment variables for current shell session
# Usage: source set-java-env.sh

export JAVA_HOME="/opt/homebrew/opt/openjdk@21"
export PATH="$JAVA_HOME/bin:$PATH"

echo "✓ Java 21 environment variables set!"
echo "JAVA_HOME=$JAVA_HOME"
echo ""
echo "Verifying:"
java -version
echo ""
javac -version
