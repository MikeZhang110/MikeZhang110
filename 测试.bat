@echo off
echo ============================================================
echo 简易测试 - 看看能否运行
echo Simple Test - See if it works
echo ============================================================
echo.

echo 您看到这些文字了吗？
echo Can you see this text?
echo.

echo 如果能看到，说明 .bat 文件可以运行。
echo If you can see this, .bat files work.
echo.

echo 正在测试 Java...
echo Testing Java...
echo.

java -version

if %errorlevel% equ 0 (
    echo.
    echo ✓ 太好了！Java 已安装！
    echo ✓ Great! Java is installed!
    echo.
    echo 现在可以尝试构建了。
    echo Now you can try building.
) else (
    echo.
    echo ✗ 问题找到了：没有安装 Java！
    echo ✗ Problem found: Java is NOT installed!
    echo.
    echo 这就是为什么双击没反应的原因。
    echo This is why double-clicking does nothing.
    echo.
    echo 请先安装 Java 17:
    echo Please install Java 17 first:
    echo.
    echo 下载: https://adoptium.net/temurin/releases/
    echo.
    echo 安装后，再次双击这个文件测试。
    echo After installation, double-click this file again to test.
)

echo.
echo ============================================================
echo 测试完成。窗口将在 30 秒后自动关闭。
echo Test complete. Window will close in 30 seconds.
echo 或按任意键立即关闭。
echo Or press any key to close now.
echo ============================================================
timeout /t 30
