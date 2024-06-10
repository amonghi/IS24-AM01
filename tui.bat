:: package .jar but skip tests
call mvnw -Dmaven.test.skip=true package
:: run the .jar
call java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005 -jar ./target/AM01-1.0-SNAPSHOT-client-tui.jar