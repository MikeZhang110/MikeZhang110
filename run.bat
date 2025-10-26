@echo off
echo ========================================
echo TranslationPro - Starting...
echo ========================================
echo.

REM Check if Java is installed
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Java is not installed or not in PATH
    echo.
    echo Please install Java 17 or higher from:
    echo https://adoptium.net/temurin/releases/
    echo.
    pause
    exit /b 1
)

echo Java found. Launching TranslationPro...
echo.

REM Run the application using Gradle
call gradlew.bat run

echo.
echo ========================================
echo TranslationPro has been closed.
echo ========================================
pause
