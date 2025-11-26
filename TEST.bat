@echo off
chcp 65001 >nul
cls
echo ============================================================
echo TranslationPro - Simple Test
echo ============================================================
echo.
echo Can you see this message?
echo If yes, the .bat file works!
echo.
echo ============================================================

echo Testing Java installation...
echo.

java -version 2>&1

if %errorlevel% equ 0 (
    echo.
    echo ============================================================
    echo SUCCESS! Java is installed!
    echo ============================================================
    echo.
    echo You can now try building the portable version.
    echo.
    echo Next step: Double-click "一键制作便携版.bat"
    echo or run: gradlew.bat clean shadowJar
    echo.
) else (
    echo.
    echo ============================================================
    echo PROBLEM FOUND: Java is NOT installed!
    echo ============================================================
    echo.
    echo This is why double-clicking does nothing.
    echo.
    echo SOLUTION:
    echo 1. Download Java 17 from:
    echo    https://adoptium.net/temurin/releases/
    echo.
    echo 2. Choose:
    echo    - Operating System: Windows
    echo    - Version: 17 - LTS
    echo    - Package Type: JDK
    echo.
    echo 3. Download the .msi file (about 180MB)
    echo.
    echo 4. Install it (check "Add to PATH" option)
    echo.
    echo 5. Restart your computer
    echo.
    echo 6. Run this test again
    echo.
)

echo ============================================================
echo Window will close in 60 seconds
echo Or press any key to close now
echo ============================================================
timeout /t 60
