@ECHO off
TITLE AionEmu - Game Server Console
:START
CLS
SET JAVAVER=1.8
SET NUMAENABLE=false
CLS
IF "%MODE%" == "" (
CALL Panel.bat
)

IF "%JAVAVER%" == "1.8" (
SET JAVA_OPTS=-XX:-UseSplitVerifier -XX:+TieredCompilation %JAVA_OPTS%
)
IF "%NUMAENABLE%" == "true" (
SET JAVA_OPTS=-XX:+UseNUMA %JAVA_OPTS%
)
ECHO Starting AionEmu Game Server in %MODE% mode.
JAVA %JAVA_OPTS% -cp ../libs/*;AionEmu-Game.jar com.aionemu.gameserver.GameServer
SET CLASSPATH=%OLDCLASSPATH%
IF ERRORLEVEL 2 GOTO START
IF ERRORLEVEL 1 GOTO ERROR
IF ERRORLEVEL 0 GOTO END
:ERROR
ECHO.
ECHO Game Server has terminated abnormaly!
ECHO.
PAUSE
EXIT
:END
ECHO.
ECHO Game Server is terminated!
ECHO.
PAUSE
EXIT