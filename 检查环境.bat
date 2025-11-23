@echo off
echo ============================================================
echo TranslationPro - 环境检测工具
echo Environment Diagnostic Tool
echo ============================================================
echo.
echo 正在检查您的系统环境...
echo Checking your system environment...
echo.
echo ============================================================

REM 检查当前目录
echo [1/5] 当前目录 / Current Directory:
echo %CD%
echo.

REM 检查重要文件是否存在
echo [2/5] 检查必需文件 / Checking required files:
echo.

if exist "build.gradle.kts" (
    echo ✓ build.gradle.kts - 找到 ^(Found^)
) else (
    echo ✗ build.gradle.kts - 未找到 ^(NOT FOUND^)
    echo   您可能不在正确的文件夹中！
    echo   You might not be in the correct folder!
)

if exist "gradlew.bat" (
    echo ✓ gradlew.bat - 找到 ^(Found^)
) else (
    echo ✗ gradlew.bat - 未找到 ^(NOT FOUND^)
    echo   Gradle wrapper 缺失！
    echo   Gradle wrapper is missing!
)

if exist "gradle\wrapper\gradle-wrapper.jar" (
    echo ✓ gradle-wrapper.jar - 找到 ^(Found^)
) else (
    echo ✗ gradle-wrapper.jar - 未找到 ^(NOT FOUND^)
    echo   需要这个文件才能构建！
    echo   This file is required to build!
)

if exist "src\main\java\com\translationpro\TranslationProApp.java" (
    echo ✓ 源代码 - 找到 ^(Found^)
) else (
    echo ✗ 源代码 - 未找到 ^(Source code NOT FOUND^)
)

echo.

REM 检查Java
echo [3/5] 检查 Java / Checking Java:
echo.

java -version >nul 2>&1
if %errorlevel% equ 0 (
    echo ✓ Java 已安装 ^(Java is installed^)
    echo.
    java -version 2>&1
) else (
    echo ✗ Java 未安装或未在 PATH 中
    echo   Java is NOT installed or not in PATH
    echo.
    echo   这是问题所在！必须先安装 Java！
    echo   This is the problem! You must install Java first!
    echo.
    echo   下载 Java 17: https://adoptium.net/temurin/releases/
)

echo.

REM 检查网络连接
echo [4/5] 检查网络连接 / Checking internet connection:
echo.

ping -n 1 google.com >nul 2>&1
if %errorlevel% equ 0 (
    echo ✓ 网络连接正常 ^(Internet connection OK^)
) else (
    echo ⚠ 无法连接到互联网
    echo   Cannot connect to internet
    echo   ^(可能只是防火墙，不一定有问题^)
)

echo.

REM 检查磁盘空间
echo [5/5] 检查磁盘空间 / Checking disk space:
echo.
for /f "tokens=3" %%a in ('dir /-c ^| find "bytes free"') do set FREE_SPACE=%%a
echo 当前磁盘剩余空间 / Free space: %FREE_SPACE% bytes
echo.

echo ============================================================
echo 诊断完成 / Diagnostic Complete
echo ============================================================
echo.

REM 给出建议
echo 建议 / Recommendations:
echo --------------------
echo.

java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo [!] 最重要：请先安装 Java 17
    echo     Most Important: Install Java 17 first
    echo.
    echo     下载地址 / Download:
    echo     https://adoptium.net/temurin/releases/
    echo.
    echo     安装步骤:
    echo     1. 选择 Windows
    echo     2. 选择 Version 17 - LTS
    echo     3. 选择 Package Type: JDK
    echo     4. 下载 .msi 文件
    echo     5. 安装时勾选 "Add to PATH"
    echo     6. 安装完成后重启命令提示符
    echo.
)

if not exist "gradlew.bat" (
    echo [!] Gradle wrapper 缺失
    echo     Missing Gradle wrapper
    echo.
    echo     解决方法:
    echo     1. 重新下载完整的 ZIP 包
    echo     2. 或使用 git clone 而不是下载 ZIP
    echo.
)

if exist "gradlew.bat" (
    if exist "build.gradle.kts" (
        java -version >nul 2>&1
        if %errorlevel% equ 0 (
            echo [√] 您的环境看起来没问题！
            echo     Your environment looks good!
            echo.
            echo     可以尝试运行构建:
            echo     You can try to build:
            echo.
            echo     1. 双击 "一键制作便携版.bat"
            echo     2. 或运行: gradlew.bat clean shadowJar
            echo.
        )
    )
)

echo.
echo ============================================================
echo 按任意键退出...
echo Press any key to exit...
pause >nul
