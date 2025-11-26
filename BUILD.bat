@echo off
chcp 65001 >nul
cls

echo ============================================================
echo TranslationPro - One-Click Portable Version Builder
echo ============================================================
echo.

REM Check current directory
echo Current directory: %CD%
echo.

REM Check Java first
echo [Step 1/4] Checking Java...
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo.
    echo ============================================================
    echo ERROR: Java is not installed!
    echo ============================================================
    echo.
    echo Java 17 or higher is required to build this project.
    echo.
    echo Download Java 17 from:
    echo https://adoptium.net/temurin/releases/
    echo.
    echo Installation steps:
    echo 1. Choose: Windows, x64, JDK, Version 17-LTS
    echo 2. Download and run the .msi installer
    echo 3. During installation, check "Add to PATH"
    echo 4. Restart your computer after installation
    echo 5. Run this script again
    echo.
    echo ============================================================
    pause
    exit /b 1
)

echo Java found!
java -version 2>&1 | findstr "version"
echo.

REM Check for Gradle wrapper
echo [Step 2/4] Checking build tools...
if not exist "gradlew.bat" (
    echo.
    echo ERROR: gradlew.bat not found!
    echo.
    echo Your download might be incomplete.
    echo Please re-download the complete ZIP package from:
    echo https://github.com/MikeZhang110/MikeZhang110
    echo.
    pause
    exit /b 1
)

echo Gradle wrapper found!
echo.

REM Build the project
echo [Step 3/4] Building application...
echo This may take 3-5 minutes on first run...
echo (Downloading dependencies and compiling code)
echo.

call gradlew.bat clean shadowJar

if %errorlevel% neq 0 (
    echo.
    echo ============================================================
    echo BUILD FAILED!
    echo ============================================================
    echo.
    echo Please check the error messages above.
    echo.
    echo Common solutions:
    echo 1. Make sure you have internet connection
    echo 2. Check if antivirus is blocking Gradle
    echo 3. Try running as administrator
    echo.
    pause
    exit /b 1
)

REM Check if JAR was created
if not exist "build\libs\translationpro-standalone.jar" (
    echo.
    echo ERROR: JAR file was not created!
    echo.
    pause
    exit /b 1
)

echo.
echo Build successful!
echo.

REM Create portable folder
echo [Step 4/4] Creating portable version folder...
echo.

set PORTABLE_DIR=TranslationPro-Portable
if exist "%PORTABLE_DIR%" (
    echo Removing old version...
    rmdir /s /q "%PORTABLE_DIR%"
)

mkdir "%PORTABLE_DIR%"
mkdir "%PORTABLE_DIR%\data"
mkdir "%PORTABLE_DIR%\logs"

echo Copying files...
copy "build\libs\translationpro-standalone.jar" "%PORTABLE_DIR%\TranslationPro.jar" >nul
if exist "LICENSE" copy "LICENSE" "%PORTABLE_DIR%\" >nul

REM Create launcher script
echo Creating launcher...
(
echo @echo off
echo cd /d "%%~dp0"
echo.
echo java -version ^>nul 2^>^&1
echo if %%errorlevel%% neq 0 ^(
echo     echo ERROR: Java not found!
echo     echo Please install Java 17 or higher from:
echo     echo https://adoptium.net/temurin/releases/
echo     pause
echo     exit /b 1
echo ^)
echo.
echo echo Starting TranslationPro...
echo start javaw -Duser.dir="%%~dp0" -jar "%%~dp0TranslationPro.jar"
) > "%PORTABLE_DIR%\Start.bat"

REM Create README
(
echo ============================================================
echo TranslationPro - Portable Green Version
echo ============================================================
echo.
echo HOW TO USE:
echo -----------
echo.
echo 1. Make sure Java 17 or higher is installed
echo    Check: java -version
echo.
echo 2. Double-click "Start.bat" to launch the application
echo.
echo 3. All data is saved in the "data" folder
echo.
echo 4. You can copy this entire folder anywhere
echo    - USB drive
echo    - External hard drive
echo    - Any location on your computer
echo.
echo FEATURES:
echo ---------
echo - No installation required
echo - No registry writes
echo - Fully portable
echo - All data in one folder
echo.
echo Java Download:
echo https://adoptium.net/temurin/releases/
echo.
) > "%PORTABLE_DIR%\README.txt"

echo.
echo ============================================================
echo SUCCESS! Portable version created!
echo ============================================================
echo.
echo Location: %CD%\%PORTABLE_DIR%
echo Size: ~30MB
echo.
echo HOW TO USE:
echo -----------
echo.
echo 1. Copy the "%PORTABLE_DIR%" folder anywhere you want
echo.
echo 2. Double-click "Start.bat" inside the folder
echo.
echo 3. Start translating!
echo.
echo ============================================================
echo.
echo Press any key to open the portable version folder...
pause >nul

explorer "%PORTABLE_DIR%"
