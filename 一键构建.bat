@echo off
chcp 65001 >nul 2>nul
cls
color 0A
echo.
echo ========================================
echo    TranslationPro - ONE CLICK BUILD
echo ========================================
echo.
echo This will automatically:
echo 1. Check Java
echo 2. Download dependencies
echo 3. Build the application
echo 4. Create portable version
echo 5. Open the result folder
echo.
echo Please wait 3-5 minutes...
echo.
echo ========================================
echo.

REM Auto detect current directory
cd /d "%~dp0"
echo Current folder: %CD%
echo.

REM Check Java
echo [Step 1/4] Checking Java...
java -version >nul 2>&1
if %errorlevel% neq 0 (
    color 0C
    echo.
    echo ERROR: Java is not installed!
    echo.
    echo Please install Java first from:
    echo https://adoptium.net/temurin/releases/
    echo.
    echo Then run this script again.
    echo.
    pause
    exit /b 1
)
echo Java: OK
java -version 2>&1 | findstr "version"
echo.

REM Build
echo [Step 2/4] Building application...
echo This may take 3-5 minutes on first run...
echo Please be patient!
echo.
call gradlew.bat clean shadowJar

if %errorlevel% neq 0 (
    color 0C
    echo.
    echo BUILD FAILED!
    echo.
    pause
    exit /b 1
)

echo.
echo [Step 3/4] Creating portable folder...
echo.

REM Create portable version
set PORTABLE_DIR=TranslationPro-Portable
if exist "%PORTABLE_DIR%" rmdir /s /q "%PORTABLE_DIR%"
mkdir "%PORTABLE_DIR%"
mkdir "%PORTABLE_DIR%\data"
mkdir "%PORTABLE_DIR%\logs"

copy "build\libs\translationpro-standalone.jar" "%PORTABLE_DIR%\TranslationPro.jar" >nul
if exist "LICENSE" copy "LICENSE" "%PORTABLE_DIR%\" >nul

REM Create launcher
(
echo @echo off
echo cd /d "%%~dp0"
echo java -version ^>nul 2^>^&1
echo if %%errorlevel%% neq 0 ^(
echo     echo Java not found! Install Java 17+ first.
echo     pause
echo     exit /b 1
echo ^)
echo start javaw -jar "%%~dp0TranslationPro.jar"
) > "%PORTABLE_DIR%\Start.bat"

REM Create README
(
echo TranslationPro - Portable Version
echo ================================
echo.
echo Double-click "Start.bat" to run the application.
echo.
echo All data is saved in the "data" folder.
echo You can copy this entire folder anywhere.
echo.
echo Requires: Java 17 or higher
) > "%PORTABLE_DIR%\README.txt"

echo [Step 4/4] Done!
echo.
echo ========================================
echo         BUILD SUCCESSFUL!
echo ========================================
echo.
echo Portable version created in:
echo %CD%\%PORTABLE_DIR%
echo.
echo Size: ~30MB
echo.
echo Opening folder in 3 seconds...
timeout /t 3 >nul
explorer "%PORTABLE_DIR%"
echo.
echo ========================================
echo.
echo Press any key to close this window...
pause >nul
