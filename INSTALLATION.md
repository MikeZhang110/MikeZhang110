# TranslationPro Installation Guide for Windows 11

This guide will help you install and run TranslationPro on your Windows 11 computer.

## Prerequisites

### 1. Install Java 17 or Higher

TranslationPro requires Java 17 (or newer) to run.

**Check if Java is already installed:**
```cmd
java -version
```

If you see "java version "17" or higher, you're good to go! Otherwise, install Java:

**Download Java:**
1. Visit: https://adoptium.net/temurin/releases/
2. Select:
   - **Operating System**: Windows
   - **Architecture**: x64
   - **Package Type**: JDK
   - **Version**: 17 - LTS (or 21 - LTS)
3. Download the `.msi` installer
4. Run the installer and follow the prompts
5. **Important**: Check the box "Add to PATH" during installation

**Verify installation:**
```cmd
java -version
javac -version
```

### 2. Verify Git (if building from source)

Git should already be installed if you cloned this repository. Verify:
```cmd
git --version
```

## Installation Options

You have three options to install TranslationPro:

---

## Option 1: Quick Start (Run with Gradle) - RECOMMENDED

This is the easiest way to get started for development and testing.

### Step 1: Navigate to the project directory

Open Command Prompt or PowerShell and navigate to where you cloned the repository:
```cmd
cd C:\path\to\MikeZhang110
```

### Step 2: Run the application

**On Windows (Command Prompt):**
```cmd
gradlew.bat run
```

**On Windows (PowerShell):**
```powershell
.\gradlew.bat run
```

That's it! The application will:
- Download all dependencies automatically
- Compile the code
- Launch TranslationPro

**First run will take 1-2 minutes** as it downloads dependencies. Subsequent runs are faster.

---

## Option 2: Create Standalone JAR

This creates a single executable JAR file that you can run anywhere.

### Step 1: Build the standalone JAR

```cmd
gradlew.bat shadowJar
```

This creates: `build\libs\translationpro-standalone.jar`

### Step 2: Run the JAR

Navigate to the build directory and run:
```cmd
java -jar build\libs\translationpro-standalone.jar
```

**Or create a convenient batch file:**

Create a file named `TranslationPro.bat` in the project root:
```batch
@echo off
java -jar "%~dp0build\libs\translationpro-standalone.jar"
pause
```

Double-click `TranslationPro.bat` to launch the application.

---

## Option 3: Create Windows Installer (Advanced)

This creates a professional Windows `.msi` installer.

### Prerequisites for Windows Installer

You need **JDK 17+** (not just JRE) for the `jpackage` tool.

### Step 1: Build the installer

```cmd
gradlew.bat createInstaller
```

### Step 2: Install

The installer will be created in the project directory. Look for:
- `TranslationPro-1.0.msi`

Double-click the `.msi` file and follow the installation wizard.

After installation:
- TranslationPro will be in your Start Menu
- Desktop shortcut created (if selected)
- Can be uninstalled via Windows Settings

---

## Option 4: Portable ZIP Distribution

Create a portable version that doesn't require installation.

### Step 1: Create portable ZIP

```cmd
gradlew.bat createPortable
```

This creates: `build\distributions\translationpro-portable-1.0.0-SNAPSHOT.zip`

### Step 2: Extract and Run

1. Extract the ZIP to any location (e.g., `C:\TranslationPro`)
2. Inside, you'll find `TranslationPro.jar`
3. Create a batch file or shortcut to run it

---

## Troubleshooting

### "java is not recognized as an internal or external command"

**Solution:** Java is not in your PATH.

1. Find your Java installation (usually `C:\Program Files\Eclipse Adoptium\jdk-17.x.x\bin`)
2. Add to PATH:
   - Press `Win + X`, select "System"
   - Click "Advanced system settings"
   - Click "Environment Variables"
   - Under "System variables", find "Path", click "Edit"
   - Click "New", add the Java `bin` directory path
   - Click OK on all dialogs
   - **Restart Command Prompt**

### "Could not find or load main class"

**Solution:** The JAR might not be built correctly.

```cmd
gradlew.bat clean shadowJar
```

### Application won't start - "No GUI"

**Solution:** You might be missing JavaFX.

This shouldn't happen as JavaFX is bundled, but if it does:
```cmd
gradlew.bat --refresh-dependencies run
```

### Build fails with "JAVA_HOME not set"

**Solution:** Set JAVA_HOME environment variable.

1. Find your Java installation directory
2. Set JAVA_HOME:
   ```cmd
   setx JAVA_HOME "C:\Program Files\Eclipse Adoptium\jdk-17.x.x"
   ```
3. Restart Command Prompt

### "Permission denied" or "Access denied"

**Solution:** Run Command Prompt as Administrator.

1. Press `Win + X`
2. Select "Windows Terminal (Admin)" or "Command Prompt (Admin)"
3. Navigate to project directory
4. Run the build/install command again

---

## Recommended Setup for Daily Use

### For Development/Testing:
```cmd
gradlew.bat run
```

### For Regular Use:

**Option A - Standalone JAR with Desktop Shortcut:**

1. Build the JAR once:
   ```cmd
   gradlew.bat shadowJar
   ```

2. Create a shortcut on your desktop:
   - Right-click Desktop → New → Shortcut
   - Location: `javaw -jar "C:\path\to\MikeZhang110\build\libs\translationpro-standalone.jar"`
   - Name: "TranslationPro"
   - Change icon if desired

**Option B - Windows Installer (Most Professional):**

1. Build installer:
   ```cmd
   gradlew.bat createInstaller
   ```

2. Install the `.msi` file
3. Use Start Menu or Desktop shortcut

---

## Quick Start After Installation

Once installed, on first launch:

1. **Welcome Screen** will appear
2. Click **"Create New Project"**
3. Enter:
   - Project name (e.g., "My First Translation")
   - Source language (e.g., "en" for English)
   - Target language (e.g., "es" for Spanish)
   - Project location
4. Click **"Import Files"** to add documents to translate
5. Start translating!

---

## Updating TranslationPro

To get the latest version:

```cmd
git pull origin claude/enhance-translation-tool-011CUVpsK1tTwYsjNX5rkKJ4
gradlew.bat clean build
```

Then run using your preferred method above.

---

## System Requirements

- **OS**: Windows 11 (also works on Windows 10, macOS, Linux)
- **Java**: 17 or higher (LTS versions recommended: 17 or 21)
- **RAM**: 2GB minimum, 4GB recommended
- **Disk Space**: 500MB
- **Display**: 1280x720 minimum resolution

---

## Files and Folders Created

TranslationPro creates the following in your user directory:

```
Project Root/
├── data/              # Translation memory database
├── logs/              # Application logs
├── plugins/           # Future: Plugin extensions
└── (your projects)    # Translation project folders
```

These are created automatically on first run.

---

## Need Help?

- Check the main [README.md](README.md) for features and usage
- Review [ARCHITECTURE.md](ARCHITECTURE.md) for technical details
- Open an issue on GitHub for bugs or feature requests

---

## Summary Commands

**First-time setup:**
```cmd
# Install Java 17+ first, then:
cd C:\path\to\MikeZhang110
gradlew.bat build
gradlew.bat run
```

**Daily use (fastest):**
```cmd
gradlew.bat run
```

**Create installer (one-time):**
```cmd
gradlew.bat createInstaller
# Then install the .msi file
```

---

Enjoy using TranslationPro! 🌍✨
