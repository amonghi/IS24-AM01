#!/bin/bash
set -e

# package .jar but skip tests
./mvnw -Dmaven.test.skip=true package

# run the .jar
echo "=== running ==="
java \
  -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005 \
  -jar ./target/AM01-1.0.0-client-tui.jar
