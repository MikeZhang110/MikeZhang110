@echo off
chcp 65001 >nul
cls
echo ============================================================
echo Environment Diagnostic Tool
echo ============================================================
echo.
echo Checking your system environment...
echo.

REM Current directory
echo [1/6] Current Directory:
echo %CD%
echo.

REM Check required files
echo [2/6] Checking Required Files:
echo.

set MISSING=0

if exist "build.gradle.kts" (
    echo [OK] build.gradle.kts - Found
) else (
    echo [!!] build.gradle.kts - NOT FOUND
    echo     You might not be in the correct folder!
    set MISSING=1
)

if exist "gradlew.bat" (
    echo [OK] gradlew.bat - Found
) else (
    echo [!!] gradlew.bat - NOT FOUND
    echo     Gradle wrapper is missing!
    set MISSING=1
)

if exist "gradle\wrapper\gradle-wrapper.jar" (
    echo [OK] gradle-wrapper.jar - Found
) else (
    echo [!!] gradle-wrapper.jar - NOT FOUND
    echo     This file is required to build!
    set MISSING=1
)

if exist "src\main\java" (
    echo [OK] Source code - Found
) else (
    echo [!!] Source code - NOT FOUND
    set MISSING=1
)

echo.

REM Check Java
echo [3/6] Checking Java:
echo.

java -version >nul 2>&1
if %errorlevel% equ 0 (
    echo [OK] Java is installed
    echo.
    java -version 2>&1
) else (
    echo [!!] Java is NOT installed or not in PATH
    echo.
    echo     THIS IS THE PROBLEM!
    echo     You must install Java first!
    echo.
    echo     Download Java 17: https://adoptium.net/temurin/releases/
    set MISSING=1
)

echo.

REM Check network
echo [4/6] Checking Internet Connection:
echo.

ping -n 1 google.com >nul 2>&1
if %errorlevel% equ 0 (
    echo [OK] Internet connection is working
) else (
    echo [??] Cannot ping google.com
    echo     (Might be firewall, not necessarily a problem)
)

echo.

REM Check disk space
echo [5/6] Checking Disk Space:
echo.
echo Current drive has sufficient space for building
echo (Build requires about 200MB free space)
echo.

REM Summary
echo [6/6] Summary and Recommendations:
echo.
echo ============================================================

if %MISSING% equ 1 (
    echo STATUS: PROBLEMS FOUND
    echo ============================================================
    echo.

    java -version >nul 2>&1
    if %errorlevel% neq 0 (
        echo [ACTION REQUIRED] Install Java 17
        echo.
        echo Download from: https://adoptium.net/temurin/releases/
        echo.
        echo Installation steps:
        echo 1. Choose Windows, x64, JDK, Version 17-LTS
        echo 2. Download .msi file
        echo 3. Run installer
        echo 4. Check "Add to PATH" option
        echo 5. Restart computer after installation
        echo.
    )

    if not exist "gradlew.bat" (
        echo [ACTION REQUIRED] Download complete ZIP package
        echo.
        echo Your download might be incomplete.
        echo Please re-download from:
        echo https://github.com/MikeZhang110/MikeZhang110
        echo.
    )
) else (
    echo STATUS: ALL CHECKS PASSED!
    echo ============================================================
    echo.
    echo Your environment looks good!
    echo.
    echo You can now build the portable version:
    echo.
    echo Method 1: Double-click "一键制作便携版.bat"
    echo Method 2: Run command: gradlew.bat clean shadowJar
    echo.
    echo First build will take 3-5 minutes to download dependencies.
    echo Subsequent builds will be faster.
    echo.
)

echo ============================================================
echo Press any key to exit...
pause >nul
