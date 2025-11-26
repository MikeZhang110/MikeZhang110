@echo off
chcp 65001 >nul 2>nul
cls
echo.
echo.
echo ============================================================
echo           TranslationPro - SIMPLE TEST
echo ============================================================
echo.
echo.
echo IF YOU CAN SEE THIS, THE .BAT FILE WORKS!
echo.
echo.
echo ============================================================
echo.
echo.
echo Now testing Java...
echo.
echo.

java -version 2>&1

echo.
echo.

if %errorlevel% equ 0 (
    echo ============================================================
    echo.
    echo     GOOD NEWS: Java is installed!
    echo.
    echo ============================================================
    echo.
    echo.
    echo Next step: Double-click BUILD.bat
    echo.
    echo.
) else (
    echo ============================================================
    echo.
    echo     PROBLEM: Java is NOT installed!
    echo.
    echo ============================================================
    echo.
    echo.
    echo This is why nothing happened before.
    echo.
    echo.
    echo YOU MUST INSTALL JAVA FIRST!
    echo.
    echo.
    echo Download from:
    echo https://adoptium.net/temurin/releases/
    echo.
    echo Choose: Windows, x64, JDK, Version 17
    echo.
    echo.
)

echo ============================================================
echo.
echo.
echo THIS WINDOW WILL STAY OPEN
echo.
echo Press ANY KEY to close this window
echo.
echo ============================================================
echo.
echo.

pause

echo.
echo Closing in 10 seconds...
timeout /t 10

exit
