@echo off
echo ============================================================
echo TranslationPro - 简易绿色便携版打包工具
echo Creating Simple Portable Version
echo ============================================================
echo.
echo 此脚本创建一个需要系统Java的轻量便携版
echo This creates a lightweight portable version that requires system Java
echo.

REM 检查Java
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo 错误: 未找到Java
    echo 请先安装Java 17: https://adoptium.net/temurin/releases/
    pause
    exit /b 1
)

echo [1/3] 构建应用程序 / Building application...
call gradlew.bat clean shadowJar
if %errorlevel% neq 0 (
    echo 构建失败 / Build failed
    pause
    exit /b 1
)
echo.

echo [2/3] 创建便携版目录 / Creating portable directory...
set PORTABLE_DIR=TranslationPro-Portable-Lite
if exist "%PORTABLE_DIR%" rmdir /s /q "%PORTABLE_DIR%"
mkdir "%PORTABLE_DIR%"
mkdir "%PORTABLE_DIR%\data"
mkdir "%PORTABLE_DIR%\logs"

REM 复制文件
copy "build\libs\translationpro-standalone.jar" "%PORTABLE_DIR%\TranslationPro.jar"
copy "LICENSE" "%PORTABLE_DIR%\"

echo [3/3] 创建启动脚本 / Creating launcher...

REM 创建启动脚本
(
echo @echo off
echo rem TranslationPro 绿色便携版启动器
echo rem TranslationPro Portable Launcher
echo.
echo cd /d "%%~dp0"
echo.
echo rem 检查Java / Check Java
echo java -version ^>nul 2^>^&1
echo if %%errorlevel%% neq 0 ^(
echo     echo ============================================================
echo     echo 错误: 未找到Java / ERROR: Java not found
echo     echo ============================================================
echo     echo.
echo     echo 请安装 Java 17 或更高版本
echo     echo Please install Java 17 or higher
echo     echo.
echo     echo 下载地址 / Download from:
echo     echo https://adoptium.net/temurin/releases/
echo     echo.
echo     echo 1. 选择 Windows / Select Windows
echo     echo 2. 版本选择 17 - LTS / Choose version 17 - LTS
echo     echo 3. 下载并安装 .msi 文件 / Download and install .msi
echo     echo 4. 安装时勾选 "Add to PATH"
echo     echo.
echo     pause
echo     exit /b 1
echo ^)
echo.
echo rem 启动程序 (使用javaw不显示控制台窗口^)
echo rem Start application (javaw hides console window^)
echo start javaw -Duser.dir="%%~dp0" -jar "%%~dp0TranslationPro.jar"
echo.
echo rem 如果上面的命令失败，使用下面的命令 (会显示控制台^)
echo rem If above fails, use this (shows console^)
echo rem java -Duser.dir="%%~dp0" -jar "%%~dp0TranslationPro.jar"
) > "%PORTABLE_DIR%\启动 TranslationPro.bat"

REM 创建英文版启动脚本
copy "%PORTABLE_DIR%\启动 TranslationPro.bat" "%PORTABLE_DIR%\Start TranslationPro.bat"

REM 创建README
(
echo ============================================================
echo TranslationPro 绿色便携版
echo TranslationPro Portable Green Version
echo ============================================================
echo.
echo 使用方法 / How to Use:
echo ---------------------
echo.
echo 1. 双击 "启动 TranslationPro.bat"
echo    Double-click "Start TranslationPro.bat"
echo.
echo 2. 第一次运行会提示安装Java^(如果未安装^)
echo    First run will prompt to install Java (if not installed^)
echo.
echo 3. 所有数据保存在此文件夹内，不写注册表
echo    All data saved in this folder, no registry writes
echo.
echo 4. 可以复制到U盘、移动硬盘或任何位置
echo    Can copy to USB drive, external drive, or anywhere
echo.
echo.
echo 文件夹说明 / Folder Structure:
echo ---------------------------
echo.
echo TranslationPro.jar   - 主程序 / Main application
echo 启动 TranslationPro.bat - 启动脚本 / Launcher script
echo data/                - 翻译记忆库和项目数据 / TM and project data
echo logs/                - 日志文件 / Log files
echo.
echo.
echo 系统要求 / System Requirements:
echo ----------------------------
echo.
echo - Windows 11/10
echo - Java 17 或更高版本 / Java 17 or higher
echo - 内存: 2GB 最小, 4GB 推荐 / RAM: 2GB min, 4GB recommended
echo - 磁盘: 500MB
echo.
echo.
echo Java 下载 / Java Download:
echo ------------------------
echo.
echo https://adoptium.net/temurin/releases/
echo.
echo 选择: Windows, JDK, Version 17-LTS
echo Select: Windows, JDK, Version 17-LTS
echo.
echo.
echo 特点 / Features:
echo ---------------
echo.
echo ✓ 绿色软件，无需安装 / Green software, no installation
echo ✓ 不写注册表 / No registry writes
echo ✓ 不需要管理员权限 / No admin rights required
echo ✓ 完全便携，即插即用 / Fully portable, plug and play
echo ✓ 数据随身带 / Take your data anywhere
echo.
echo.
echo 帮助 / Help:
echo ----------
echo.
echo 项目主页 / Project: https://github.com/MikeZhang110/MikeZhang110
echo 问题报告 / Issues: https://github.com/MikeZhang110/MikeZhang110/issues
echo.
echo ============================================================
) > "%PORTABLE_DIR%\使用说明 README.txt"

echo.
echo ============================================================
echo 打包完成！ / Packaging Complete!
echo ============================================================
echo.
echo 位置 / Location: %CD%\%PORTABLE_DIR%
echo 大小 / Size: ~30MB
echo.
echo 使用方法 / Usage:
echo.
echo 1. 将 %PORTABLE_DIR% 文件夹复制到任何位置
echo    Copy %PORTABLE_DIR% folder anywhere
echo.
echo 2. 确保已安装 Java 17 (如果没有会提示下载)
echo    Ensure Java 17 is installed (will prompt if not)
echo.
echo 3. 双击 "启动 TranslationPro.bat"
echo    Double-click "Start TranslationPro.bat"
echo.
echo 4. 开始使用！
echo    Start using!
echo.
echo.
echo 是否创建ZIP压缩包? / Create ZIP? (Y/N)
set /p CREATE_ZIP=

if /i "%CREATE_ZIP%"=="Y" (
    echo.
    echo 创建ZIP... / Creating ZIP...
    powershell -command "Compress-Archive -Path '%PORTABLE_DIR%' -DestinationPath 'TranslationPro-Portable-Lite.zip' -Force"
    if %errorlevel% equ 0 (
        echo.
        echo ✓ ZIP已创建 / ZIP created: TranslationPro-Portable-Lite.zip
        echo.
        echo 您可以分享这个ZIP文件给其他人
        echo You can share this ZIP file with others
    )
)

echo.
echo ============================================================
pause
