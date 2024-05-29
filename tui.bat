
:: package .jar but skip tests
call .\mvnw.cmd -Dmaven.test.skip=true package

:: run the .jar
echo "=== running ==="
java ^
  -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005 ^
  -cp .\target\AM01-1.0-SNAPSHOT.jar it.polimi.ingsw.am01.tui.Main
