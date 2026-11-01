@echo off
rem Starts a RuneLite development client with the Low Res GPU plugin, using Java 21.
rem Your system-wide Java setting is not changed.
setlocal
cd /d "%~dp0"

rem Use JAVA21_HOME if you set it yourself, otherwise look in the usual install folders
if defined JAVA21_HOME (
	set "JDK=%JAVA21_HOME%"
	goto found
)

set "JDK="
for /d %%D in ("%ProgramFiles%\Java\jdk-21*" "%ProgramFiles%\Eclipse Adoptium\jdk-21*" "%ProgramFiles%\Microsoft\jdk-21*" "%ProgramFiles%\Zulu\zulu-21*" "%ProgramFiles%\Amazon Corretto\jdk21*") do set "JDK=%%D"
if defined JDK goto found

echo Could not find Java 21.
echo Install a Java 21 JDK, for example from https://adoptium.net/temurin/releases/?version=21
echo or set JAVA21_HOME to its folder, then run this again.
pause
exit /b 1

:found
if not exist "%JDK%\bin\java.exe" (
	echo "%JDK%" does not look like a Java install, bin\java.exe is missing.
	pause
	exit /b 1
)
echo Using Java from %JDK%
set "JAVA_HOME=%JDK%"
call gradlew.bat --stop >nul 2>&1
call gradlew.bat run
echo.
pause
