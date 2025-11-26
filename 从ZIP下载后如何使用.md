# 从 GitHub ZIP 下载后如何使用

## ⚠️ 重要提示

如果您是从 GitHub 下载的 ZIP 包，直接运行 `create-portable.bat` **可能会失败**！

这是因为：
1. ZIP 包不包含 Gradle Wrapper 文件
2. 某些依赖文件被 .gitignore 排除

## ✅ 推荐方案（3个选择）

---

### 方案1：使用 Git Clone（最佳）⭐⭐⭐⭐⭐

这是**最简单、最可靠**的方法！

**步骤：**

1. **安装 Git**（如果还没有）
   - 下载：https://git-scm.com/download/win
   - 安装时选择默认选项即可

2. **打开命令提示符**
   - 按 Win + R
   - 输入 `cmd`
   - 按回车

3. **克隆仓库**
   ```cmd
   cd C:\
   git clone https://github.com/MikeZhang110/MikeZhang110.git
   cd MikeZhang110
   ```

4. **创建便携版**
   ```cmd
   双击: 一键制作便携版.bat
   ```

**完成！** 🎉

---

### 方案2：手动安装 Gradle（如果您想从 ZIP 构建）⭐⭐⭐

**步骤：**

1. **安装 Java 17**
   - 下载：https://adoptium.net/temurin/releases/
   - 选择 Windows, JDK, Version 17
   - 安装时勾选 "Add to PATH"

2. **安装 Gradle**
   - 下载：https://gradle.org/releases/
   - 选择最新版本（8.5 或更高）
   - 下载 binary-only 版本
   - 解压到 C:\Gradle
   - 添加到系统 PATH：
     - 右键"此电脑" → 属性 → 高级系统设置
     - 环境变量 → 系统变量 → Path
     - 新建 → 添加 `C:\Gradle\gradle-8.x\bin`

3. **验证安装**
   ```cmd
   java -version
   gradle -version
   ```

4. **构建便携版**
   - 进入解压后的 MikeZhang110 文件夹
   - 双击 `一键制作便携版.bat`

---

### 方案3：等待预构建版本发布⭐⭐⭐⭐

我们计划发布预构建的便携版本，您可以直接下载使用，无需任何构建步骤。

**关注 GitHub Releases**：
```
https://github.com/MikeZhang110/MikeZhang110/releases
```

预构建版本将包括：
- TranslationPro-Portable-Full.zip（含 JRE，~100MB）
- TranslationPro-Portable-Lite.zip（需要 Java，~30MB）

---

## 🔧 问题排查

### Q: 双击 .bat 文件没反应

**解决方法：**

1. **右键点击 .bat 文件**
2. 选择 "编辑"
3. 检查文件内容是否完整
4. 或者用命令行运行：
   ```cmd
   cd C:\path\to\MikeZhang110
   一键制作便携版.bat
   ```

### Q: 提示"找不到 gradlew.bat"

**原因：** ZIP 下载不包含这个文件

**解决方法：**
- 使用方案1（Git Clone）
- 或使用方案2（手动安装 Gradle）

### Q: 提示"Java 未找到"

**解决方法：**

1. 安装 Java 17：https://adoptium.net/temurin/releases/
2. 验证安装：
   ```cmd
   java -version
   ```
3. 如果仍然找不到，重启命令提示符

### Q: 构建过程中出错

**解决方法：**

1. 确保网络连接正常（需要下载依赖）
2. 清理后重试：
   ```cmd
   gradlew clean
   gradlew shadowJar
   ```
3. 检查防火墙是否阻止了 Gradle

---

## 📦 最简单的方法（总结）

```cmd
# 1. 安装 Git（一次性）
# 下载：https://git-scm.com/download/win

# 2. 克隆项目
git clone https://github.com/MikeZhang110/MikeZhang110.git
cd MikeZhang110

# 3. 创建便携版
双击: 一键制作便携版.bat

# 4. 完成！
```

**时间：** 首次 3-5 分钟，之后每次 1-2 分钟

---

## 📖 更多帮助

- **详细安装指南**：[INSTALLATION.md](INSTALLATION.md)
- **绿色便携版说明**：[绿色便携版说明.md](绿色便携版说明.md)
- **项目说明**：[README.md](README.md)

---

## 💡 为什么 Git Clone 比 ZIP 下载好？

| 特性 | Git Clone | ZIP 下载 |
|------|-----------|----------|
| 包含完整文件 | ✅ 是 | ❌ 否 |
| 可以更新 | ✅ `git pull` | ❌ 需重新下载 |
| 保持历史 | ✅ 是 | ❌ 否 |
| 直接可用 | ✅ 是 | ⚠️ 需额外步骤 |
| 文件完整性 | ✅ 保证 | ⚠️ 可能缺失 |

**推荐：** 使用 Git Clone！

---

**如有问题，欢迎提 Issue：**
https://github.com/MikeZhang110/MikeZhang110/issues

祝您使用愉快！🌍✨
