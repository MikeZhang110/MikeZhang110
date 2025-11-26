@echo off
setlocal enabledelayedexpansion

echo ============================================================
echo TranslationPro - 绿色便携版打包工具
echo Creating Portable Green Version
echo ============================================================
echo.

REM 检查Java是否安装
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo 错误: 未找到Java，请先安装JDK 17或更高版本
    echo ERROR: Java not found. Please install JDK 17 or higher
    echo.
    echo 下载地址: https://adoptium.net/temurin/releases/
    pause
    exit /b 1
)

echo [1/5] 检查JDK工具 / Checking JDK tools...
where jlink >nul 2>&1
if %errorlevel% neq 0 (
    echo 警告: 未找到jlink，将创建不含JRE的便携版
    echo Warning: jlink not found, will create portable version without JRE
    set INCLUDE_JRE=0
) else (
    echo 找到jlink，将创建包含JRE的完整便携版
    echo Found jlink, will create full portable version with JRE
    set INCLUDE_JRE=1
)
echo.

echo [2/5] 构建应用程序JAR / Building application JAR...
call gradlew.bat clean shadowJar
if %errorlevel% neq 0 (
    echo 构建失败 / Build failed
    pause
    exit /b 1
)
echo.

echo [3/5] 创建便携版目录结构 / Creating portable directory structure...
set PORTABLE_DIR=TranslationPro-Portable
if exist "%PORTABLE_DIR%" rmdir /s /q "%PORTABLE_DIR%"
mkdir "%PORTABLE_DIR%"
mkdir "%PORTABLE_DIR%\app"
mkdir "%PORTABLE_DIR%\data"
mkdir "%PORTABLE_DIR%\logs"
echo.

echo [4/5] 复制应用程序文件 / Copying application files...
copy "build\libs\translationpro-standalone.jar" "%PORTABLE_DIR%\app\TranslationPro.jar"
copy "LICENSE" "%PORTABLE_DIR%\"
echo.

if %INCLUDE_JRE%==1 (
    echo [5/5] 创建精简JRE / Creating minimal JRE with jlink...
    echo 这可能需要几分钟 / This may take a few minutes...

    REM 创建自定义JRE，只包含需要的模块
    jlink --add-modules java.base,java.desktop,java.sql,java.naming,java.management,java.logging,jdk.unsupported,jdk.crypto.ec ^
          --strip-debug ^
          --no-man-pages ^
          --no-header-files ^
          --compress=2 ^
          --output "%PORTABLE_DIR%\jre"

    if %errorlevel% neq 0 (
        echo jlink创建JRE失败，将创建不含JRE的版本
        echo jlink failed, creating version without JRE
        rmdir /s /q "%PORTABLE_DIR%\jre"
        set INCLUDE_JRE=0
    )
    echo.
)

echo 创建启动脚本 / Creating launcher scripts...

REM 创建带JRE的启动脚本
if %INCLUDE_JRE%==1 (
    echo @echo off > "%PORTABLE_DIR%\TranslationPro.bat"
    echo cd /d "%%~dp0" >> "%PORTABLE_DIR%\TranslationPro.bat"
    echo start jre\bin\javaw.exe -jar "app\TranslationPro.jar" >> "%PORTABLE_DIR%\TranslationPro.bat"

    echo 创建了包含JRE的完整便携版
    echo Created full portable version with bundled JRE
) else (
    REM 创建需要系统Java的启动脚本
    echo @echo off > "%PORTABLE_DIR%\TranslationPro.bat"
    echo cd /d "%%~dp0" >> "%PORTABLE_DIR%\TranslationPro.bat"
    echo java -version ^>nul 2^>^&1 >> "%PORTABLE_DIR%\TranslationPro.bat"
    echo if %%errorlevel%% neq 0 ( >> "%PORTABLE_DIR%\TranslationPro.bat"
    echo     echo 错误: 未找到Java >> "%PORTABLE_DIR%\TranslationPro.bat"
    echo     echo ERROR: Java not found >> "%PORTABLE_DIR%\TranslationPro.bat"
    echo     echo. >> "%PORTABLE_DIR%\TranslationPro.bat"
    echo     echo 请安装Java 17或更高版本 >> "%PORTABLE_DIR%\TranslationPro.bat"
    echo     echo Please install Java 17 or higher >> "%PORTABLE_DIR%\TranslationPro.bat"
    echo     echo 下载: https://adoptium.net/temurin/releases/ >> "%PORTABLE_DIR%\TranslationPro.bat"
    echo     pause >> "%PORTABLE_DIR%\TranslationPro.bat"
    echo     exit /b 1 >> "%PORTABLE_DIR%\TranslationPro.bat"
    echo ) >> "%PORTABLE_DIR%\TranslationPro.bat"
    echo start javaw -jar "app\TranslationPro.jar" >> "%PORTABLE_DIR%\TranslationPro.bat"

    echo 创建了需要系统Java的便携版
    echo Created portable version (requires system Java)
)

REM 创建README
echo # TranslationPro 绿色便携版 > "%PORTABLE_DIR%\README.txt"
echo # TranslationPro Portable Green Version >> "%PORTABLE_DIR%\README.txt"
echo. >> "%PORTABLE_DIR%\README.txt"
echo 使用方法 / How to Use: >> "%PORTABLE_DIR%\README.txt"
echo ================== >> "%PORTABLE_DIR%\README.txt"
echo. >> "%PORTABLE_DIR%\README.txt"

if %INCLUDE_JRE%==1 (
    echo 1. 双击 TranslationPro.bat 启动程序 >> "%PORTABLE_DIR%\README.txt"
    echo    Double-click TranslationPro.bat to start >> "%PORTABLE_DIR%\README.txt"
    echo. >> "%PORTABLE_DIR%\README.txt"
    echo 2. 无需安装Java，所有内容已包含 >> "%PORTABLE_DIR%\README.txt"
    echo    No Java installation required, everything is included >> "%PORTABLE_DIR%\README.txt"
    echo. >> "%PORTABLE_DIR%\README.txt"
    echo 3. 可以复制整个文件夹到任何位置 >> "%PORTABLE_DIR%\README.txt"
    echo    You can copy the entire folder anywhere >> "%PORTABLE_DIR%\README.txt"
    echo. >> "%PORTABLE_DIR%\README.txt"
    echo 4. 所有数据保存在 data 文件夹中 >> "%PORTABLE_DIR%\README.txt"
    echo    All data is saved in the data folder >> "%PORTABLE_DIR%\README.txt"
) else (
    echo 1. 确保已安装 Java 17 或更高版本 >> "%PORTABLE_DIR%\README.txt"
    echo    Make sure Java 17 or higher is installed >> "%PORTABLE_DIR%\README.txt"
    echo    下载: https://adoptium.net/temurin/releases/ >> "%PORTABLE_DIR%\README.txt"
    echo. >> "%PORTABLE_DIR%\README.txt"
    echo 2. 双击 TranslationPro.bat 启动程序 >> "%PORTABLE_DIR%\README.txt"
    echo    Double-click TranslationPro.bat to start >> "%PORTABLE_DIR%\README.txt"
    echo. >> "%PORTABLE_DIR%\README.txt"
    echo 3. 可以复制整个文件夹到任何位置 >> "%PORTABLE_DIR%\README.txt"
    echo    You can copy the entire folder anywhere >> "%PORTABLE_DIR%\README.txt"
)
echo. >> "%PORTABLE_DIR%\README.txt"
echo 文件夹说明 / Folder Structure: >> "%PORTABLE_DIR%\README.txt"
echo ========================= >> "%PORTABLE_DIR%\README.txt"
echo app/     - 应用程序文件 / Application files >> "%PORTABLE_DIR%\README.txt"
if %INCLUDE_JRE%==1 (
    echo jre/     - Java运行环境 / Java Runtime Environment >> "%PORTABLE_DIR%\README.txt"
)
echo data/    - 数据文件夹 / Data folder >> "%PORTABLE_DIR%\README.txt"
echo logs/    - 日志文件夹 / Logs folder >> "%PORTABLE_DIR%\README.txt"

echo.
echo ============================================================
echo 打包完成！ / Packaging Complete!
echo ============================================================
echo.
echo 便携版位置 / Portable version location:
echo %CD%\%PORTABLE_DIR%
echo.

if %INCLUDE_JRE%==1 (
    echo 包含内容 / Contents:
    echo - 应用程序 / Application
    echo - Java运行环境 (JRE) / Java Runtime Environment
    echo - 启动脚本 / Launcher script
    echo.
    echo 大小 / Size: ~50-100 MB
    echo.
    echo 使用方法 / How to use:
    echo 1. 将整个 %PORTABLE_DIR% 文件夹复制到任何位置
    echo    Copy the entire %PORTABLE_DIR% folder anywhere
    echo.
    echo 2. 双击 TranslationPro.bat 启动
    echo    Double-click TranslationPro.bat to start
    echo.
    echo 3. 无需安装，无需管理员权限，不写注册表
    echo    No installation, no admin rights, no registry writes
) else (
    echo 包含内容 / Contents:
    echo - 应用程序 / Application
    echo - 启动脚本 / Launcher script
    echo.
    echo 大小 / Size: ~30 MB
    echo.
    echo 注意 / Note:
    echo 需要系统已安装 Java 17 或更高版本
    echo Requires Java 17 or higher installed on the system
    echo.
    echo 使用方法 / How to use:
    echo 1. 确保已安装 Java
    echo    Make sure Java is installed
    echo.
    echo 2. 将整个 %PORTABLE_DIR% 文件夹复制到任何位置
    echo    Copy the entire %PORTABLE_DIR% folder anywhere
    echo.
    echo 3. 双击 TranslationPro.bat 启动
    echo    Double-click TranslationPro.bat to start
)

echo.
echo 是否创建ZIP压缩包? / Create ZIP archive? (Y/N)
set /p CREATE_ZIP=

if /i "%CREATE_ZIP%"=="Y" (
    echo.
    echo 正在创建ZIP压缩包... / Creating ZIP archive...
    powershell -command "Compress-Archive -Path '%PORTABLE_DIR%' -DestinationPath 'TranslationPro-Portable.zip' -Force"
    if %errorlevel% equ 0 (
        echo.
        echo ZIP创建成功！/ ZIP created successfully!
        echo 位置 / Location: %CD%\TranslationPro-Portable.zip
    )
)

echo.
echo ============================================================
echo 完成！/ Done!
echo ============================================================
pause
