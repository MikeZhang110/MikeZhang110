@echo off
echo ========================================
echo TranslationPro - Building Standalone JAR
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

echo Building standalone JAR...
echo This may take a few minutes on first run...
echo.

REM Build the standalone JAR
call gradlew.bat shadowJar

if %errorlevel% equ 0 (
    echo.
    echo ========================================
    echo BUILD SUCCESSFUL!
    echo ========================================
    echo.
    echo Standalone JAR created at:
    echo build\libs\translationpro-standalone.jar
    echo.
    echo To run it:
    echo java -jar build\libs\translationpro-standalone.jar
    echo.
    echo Or double-click: run-standalone.bat
    echo ========================================
) else (
    echo.
    echo ========================================
    echo BUILD FAILED!
    echo ========================================
    echo Please check the error messages above.
)

echo.
pause
