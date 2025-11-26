@echo off
echo ========================================
echo TranslationPro - Running Standalone JAR
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

REM Check if JAR exists
if not exist "build\libs\translationpro-standalone.jar" (
    echo ERROR: Standalone JAR not found!
    echo.
    echo Please build it first by running:
    echo build-standalone.bat
    echo.
    pause
    exit /b 1
)

echo Starting TranslationPro...
echo.

REM Run the standalone JAR (javaw for no console window)
start javaw -jar "%~dp0build\libs\translationpro-standalone.jar"

echo TranslationPro launched successfully!
echo You can close this window.
timeout /t 3
