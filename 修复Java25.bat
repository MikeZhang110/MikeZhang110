@echo off
echo Creating compatibility fix for Java 25...
echo.

cd /d "%~dp0"

echo Updating Gradle wrapper to support Java 25...
call gradlew.bat wrapper --gradle-version=8.6

echo.
echo Fix applied! Now run: 一键构建.bat
echo.
pause
