#!/bin/bash
# Starts a RuneLite development client with the Low Res GPU plugin on macOS, using Java 21.
# Your system-wide Java setting is not changed. Double-click this file in Finder to run it.
cd "$(dirname "$0")" || exit 1

# Use JAVA21_HOME if you set it yourself, otherwise ask macOS where Java 21 is installed
if [ -n "$JAVA21_HOME" ]; then
	JDK="$JAVA21_HOME"
else
	JDK="$(/usr/libexec/java_home -v 21 2>/dev/null)"
fi

if [ -z "$JDK" ] || [ ! -x "$JDK/bin/java" ]; then
	echo "Could not find Java 21."
	echo "Install a Java 21 JDK, for example from https://adoptium.net/temurin/releases/?version=21&os=mac"
	echo "or set JAVA21_HOME to its folder, then run this again."
	read -r -p "Press Enter to close..."
	exit 1
fi

echo "Using Java from $JDK"
export JAVA_HOME="$JDK"
chmod +x ./gradlew 2>/dev/null
./gradlew --stop >/dev/null 2>&1
./gradlew run
echo
read -r -p "Press Enter to close..."
