@echo off

IF [%1]==[] GOTO NO_ARGUMENT
IF [%1]==[-a] GOTO ALL
IF [%1]==[-c] GOTO COMPILE
IF [%1]==[-d] GOTO DEBUG
IF [%1]==[-h] GOTO HELP

:HELP
echo Help:
echo * tui.bat
echo     Runs tui jar file
echo * tui.bat -c
echo     Runs and compiles jar file using maven
echo * tui.bat -d
echo     Enable JVM debugger mode and run jar file
echo * tui.bat -a
echo     Does all the above
echo * tui.bat -h
echo     Print option descriptions
GOTO QUIT

:NO_ARGUMENT
SET command="chcp 65001 && java -jar ./target/AM01-1.0.0-client-tui.jar"
GOTO DONE

:ALL
SET command="chcp 65001 && mvnw -Dmaven.test.skip=true package && java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005 -jar ./target/AM01-1.0.0-client-tui.jar"
GOTO DONE

:COMPILE
SET command="chcp 65001 && mvnw -Dmaven.test.skip=true package && java -jar ./target/AM01-1.0.0-client-tui.jar"
GOTO DONE

:DEBUG
SET command="chcp 65001 && java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005 -jar ./target/AM01-1.0.0-client-tui.jar"
GOTO DONE

:DONE
wt -d "%cd%" cmd /k %command%

:QUIT