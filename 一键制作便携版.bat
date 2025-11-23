@echo off
setlocal enabledelayedexpansion

echo ============================================================
echo TranslationPro - 一键绿色便携版制作工具
echo One-Click Portable Version Creator
echo ============================================================
echo.

REM 检查当前目录
echo 当前目录 / Current directory: %CD%
echo.

REM 检查Java
echo [步骤 1/4] 检查Java环境 / Checking Java...
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo.
    echo ============================================================
    echo 错误：未找到Java！
    echo ERROR: Java not found!
    echo ============================================================
    echo.
    echo 请先安装 Java 17 或更高版本
    echo Please install Java 17 or higher first
    echo.
    echo 下载地址 / Download from:
    echo https://adoptium.net/temurin/releases/
    echo.
    echo 安装步骤:
    echo 1. 选择 Windows
    echo 2. 选择 Version 17 - LTS
    echo 3. 选择 Package Type: JDK
    echo 4. 下载 .msi 文件
    echo 5. 安装时勾选 "Add to PATH"
    echo.
    pause
    exit /b 1
)

echo ✓ Java 已安装 / Java installed
java -version
echo.

REM 检查是否有 Gradle wrapper
echo [步骤 2/4] 检查构建工具 / Checking build tools...
if not exist "gradlew.bat" (
    echo.
    echo 警告：未找到 Gradle wrapper
    echo Warning: Gradle wrapper not found
    echo.
    echo 这通常是因为您从GitHub下载了ZIP包。
    echo This usually happens when you downloaded ZIP from GitHub.
    echo.
    echo 正在初始化 Gradle wrapper...
    echo Initializing Gradle wrapper...
    echo.

    REM 尝试使用系统 Gradle
    where gradle >nul 2>&1
    if %errorlevel% equ 0 (
        echo 找到系统 Gradle，正在生成 wrapper...
        echo Found system Gradle, generating wrapper...
        gradle wrapper --gradle-version 8.5
        if %errorlevel% neq 0 (
            echo Gradle wrapper 生成失败
            goto NO_GRADLE
        )
    ) else (
        goto NO_GRADLE
    )
) else (
    echo ✓ Gradle wrapper 已就绪 / Gradle wrapper ready
)
echo.

REM 构建项目
echo [步骤 3/4] 构建应用程序 / Building application...
echo 这可能需要几分钟... / This may take a few minutes...
echo.

call gradlew.bat clean shadowJar
if %errorlevel% neq 0 (
    echo.
    echo 构建失败！/ Build failed!
    echo.
    pause
    exit /b 1
)
echo.

REM 检查JAR是否生成
if not exist "build\libs\translationpro-standalone.jar" (
    echo.
    echo 错误：JAR文件未生成
    echo ERROR: JAR file not generated
    echo.
    pause
    exit /b 1
)

echo [步骤 4/4] 创建便携版文件夹 / Creating portable folder...
echo.

REM 创建便携版目录
set PORTABLE_DIR=TranslationPro-Portable-Simple
if exist "%PORTABLE_DIR%" (
    echo 删除旧版本... / Removing old version...
    rmdir /s /q "%PORTABLE_DIR%"
)

mkdir "%PORTABLE_DIR%"
mkdir "%PORTABLE_DIR%\data"
mkdir "%PORTABLE_DIR%\logs"

REM 复制文件
echo 复制文件... / Copying files...
copy "build\libs\translationpro-standalone.jar" "%PORTABLE_DIR%\TranslationPro.jar" >nul
if exist "LICENSE" copy "LICENSE" "%PORTABLE_DIR%\" >nul

REM 创建启动脚本
echo 创建启动脚本... / Creating launcher...
(
echo @echo off
echo rem TranslationPro 绿色便携版
echo rem 请确保已安装 Java 17 或更高版本
echo.
echo cd /d "%%~dp0"
echo.
echo java -version ^>nul 2^>^&1
echo if %%errorlevel%% neq 0 ^(
echo     echo ============================================================
echo     echo 错误: 未找到Java
echo     echo ERROR: Java not found
echo     echo ============================================================
echo     echo.
echo     echo 请安装 Java 17 或更高版本
echo     echo Please install Java 17 or higher
echo     echo.
echo     echo 下载: https://adoptium.net/temurin/releases/
echo     echo.
echo     pause
echo     exit /b 1
echo ^)
echo.
echo echo 正在启动 TranslationPro...
echo echo Starting TranslationPro...
echo start javaw -Duser.dir="%%~dp0" -jar "%%~dp0TranslationPro.jar"
) > "%PORTABLE_DIR%\启动 TranslationPro.bat"

copy "%PORTABLE_DIR%\启动 TranslationPro.bat" "%PORTABLE_DIR%\Start TranslationPro.bat" >nul

REM 创建README
(
echo ============================================================
echo TranslationPro 绿色便携版
echo TranslationPro Portable Green Version
echo ============================================================
echo.
echo 使用方法 / How to Use:
echo -----------------------
echo.
echo 1. 确保已安装 Java 17 或更高版本
echo    Make sure Java 17+ is installed
echo.
echo 2. 双击 "启动 TranslationPro.bat"
echo    Double-click "Start TranslationPro.bat"
echo.
echo 3. 开始使用！
echo    Start using!
echo.
echo Java 下载 / Java Download:
echo https://adoptium.net/temurin/releases/
echo.
echo 特点 / Features:
echo ---------------
echo ✓ 无需安装 / No installation
echo ✓ 不写注册表 / No registry writes
echo ✓ 完全便携 / Fully portable
echo ✓ 数据随身 / Data with you
echo.
) > "%PORTABLE_DIR%\使用说明.txt"

echo.
echo ============================================================
echo 制作成功！/ Success!
echo ============================================================
echo.
echo 便携版位置 / Location:
echo %CD%\%PORTABLE_DIR%
echo.
echo 文件夹大小 / Size: ~30MB
echo.
echo 使用方法 / How to use:
echo ----------------------
echo 1. 将 %PORTABLE_DIR% 文件夹复制到任意位置
echo    Copy the folder anywhere you want
echo.
echo 2. 双击 "启动 TranslationPro.bat"
echo    Double-click "Start TranslationPro.bat"
echo.
echo 3. 开始翻译！
echo    Start translating!
echo.
echo ============================================================
pause
exit /b 0

:NO_GRADLE
echo.
echo ============================================================
echo 方案A：无法自动构建
echo Option A: Cannot build automatically
echo ============================================================
echo.
echo 您需要先安装 Gradle 或使用 git clone
echo You need to install Gradle or use git clone
echo.
echo 推荐方案B：下载预构建版本（如果可用）
echo Recommended Option B: Download pre-built version (if available)
echo.
echo 或者方案C：使用 git clone 而不是下载ZIP
echo Or Option C: Use git clone instead of downloading ZIP
echo.
echo Git clone 命令:
echo git clone https://github.com/MikeZhang110/MikeZhang110.git
echo cd MikeZhang110
echo %~nx0
echo.
echo ============================================================
pause
exit /b 1
