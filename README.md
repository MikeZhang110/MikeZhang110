# TranslationPro

> Professional Computer-Assisted Translation Tool for Windows 11

TranslationPro is a next-generation CAT (Computer-Assisted Translation) tool based on OmegaT's open-source principles, designed to surpass tools like Wordfast Pro 3 with modern architecture, superior performance, and extensive features.

## Features

### Core Translation Capabilities

- **Advanced Translation Memory (TM)**
  - H2 database-backed storage for millions of translation units
  - Fuzzy matching with Levenshtein distance algorithm
  - Context-aware matching (considers previous/next segments)
  - Subsegment matching for partial reuse
  - Quality scoring and usage tracking
  - TMX 1.4b import/export (planned)

- **Intelligent Segmentation**
  - ICU4J-based text segmentation
  - Language-aware sentence breaking
  - Customizable segmentation rules
  - Support for multiple file formats

- **Project Management**
  - Flexible project structure
  - Multiple target languages per project
  - Project-specific settings and configuration
  - Recent projects tracking
  - JSON-based project files

### Modern Architecture

- **Technology Stack**
  - Java 17 (LTS with modern features)
  - JavaFX 21 for modern UI
  - H2 embedded database
  - Google Guice for dependency injection
  - Gradle 8.x build system

- **Design Principles**
  - Plugin-first architecture
  - Separation of concerns
  - Dependency injection
  - Event-driven communication
  - Extensive logging

### User Interface

- **Modern JavaFX Interface**
  - Clean, intuitive design
  - Tabbed editor interface
  - Menu and toolbar navigation
  - Welcome screen with recent projects
  - Professional styling with modern-light theme

- **Future UI Features** (Planned)
  - Split-screen editing (source/target side-by-side)
  - Dockable panels
  - Dark mode
  - Customizable layouts
  - Multi-monitor support

### Advanced Features (Planned)

- **Machine Translation Integration**
  - Google Translate
  - DeepL
  - Azure Translator
  - OpenAI GPT-4
  - Offline models

- **Quality Assurance**
  - 15+ built-in QA checks
  - Consistency checking
  - Number and punctuation validation
  - Term consistency
  - Spell and grammar checking

- **Terminology Management**
  - Multi-glossary support
  - Automatic term extraction
  - Forbidden term detection
  - Contextual highlighting

- **File Format Support**
  - Text files (.txt)
  - HTML/XML
  - Properties files
  - JSON
  - Microsoft Office (planned)
  - XLIFF 1.2/2.0 (planned)
  - Adobe InDesign IDML (planned)

## Getting Started

### 🎯 Quick Start - Portable Green Version (推荐绿色便携版)

**Want a portable version that doesn't require installation?**

TranslationPro offers a true "green software" portable version:
- ✅ No installation required - just unzip and run
- ✅ No registry writes - leaves no traces on your system
- ✅ Fully portable - carry on USB drive
- ✅ All data stays in the application folder

**Create Portable Version:**

**Option 1 - Full Version (with bundled Java, ~100MB):**
```cmd
# Double-click this file:
create-portable.bat
```
Best for sharing or use on any computer.

**Option 2 - Lite Version (requires system Java, ~30MB):**
```cmd
# Double-click this file:
create-portable-simple.bat
```
Smaller size, requires Java 17 installed.

**📖 Detailed Guide:** See [绿色便携版说明.md](绿色便携版说明.md) (Chinese)

---

### Prerequisites

- Java 17 or higher
- Windows 11 (primary target, but works on Windows 10, macOS, and Linux)
- 2GB RAM minimum (4GB recommended)
- 500MB disk space

### Building from Source

1. Clone the repository:
```bash
git clone https://github.com/MikeZhang110/translationpro.git
cd translationpro
```

2. Build with Gradle:
```bash
./gradlew build
```

3. Run the application:
```bash
./gradlew run
```

4. Create standalone JAR:
```bash
./gradlew shadowJar
```

The standalone JAR will be in `build/libs/translationpro-standalone.jar`

### Creating Windows Installer

```bash
./gradlew createInstaller
```

This creates a Windows MSI installer using jpackage.

### Creating Portable Distribution

```bash
./gradlew createPortable
```

This creates a portable ZIP file that can be extracted and run anywhere.

## Project Structure

```
translationpro/
├── src/main/java/com/translationpro/
│   ├── TranslationProApp.java          # Main application entry point
│   ├── core/
│   │   ├── CoreModule.java             # Dependency injection configuration
│   │   ├── tm/                         # Translation Memory
│   │   │   ├── TranslationMemoryService.java
│   │   │   ├── H2TranslationMemoryService.java
│   │   │   ├── TranslationUnit.java
│   │   │   ├── FuzzyMatch.java
│   │   │   └── TMStatistics.java
│   │   ├── project/                    # Project Management
│   │   │   ├── ProjectService.java
│   │   │   ├── ProjectServiceImpl.java
│   │   │   ├── Project.java
│   │   │   ├── ProjectSettings.java
│   │   │   ├── ProjectStatistics.java
│   │   │   └── ProjectStructure.java
│   │   └── document/                   # Document Processing
│   │       ├── DocumentService.java
│   │       ├── DocumentServiceImpl.java
│   │       ├── Document.java
│   │       ├── Segment.java
│   │       └── Tag.java
│   ├── services/                       # Services layer (planned)
│   │   ├── mt/                         # Machine Translation
│   │   ├── terminology/                # Terminology Management
│   │   └── qa/                         # Quality Assurance
│   ├── ui/                             # User Interface
│   │   └── MainWindow.java
│   ├── plugin/                         # Plugin System (planned)
│   └── util/                           # Utilities
│       └── FuzzyMatcher.java
├── src/main/resources/
│   ├── application.properties          # Application configuration
│   ├── logback.xml                     # Logging configuration
│   └── styles/
│       └── modern-light.css            # UI stylesheet
├── build.gradle.kts                    # Build configuration
├── settings.gradle.kts                 # Gradle settings
├── ARCHITECTURE.md                     # Detailed architecture documentation
└── README.md                           # This file
```

## Architecture

TranslationPro is built with a layered architecture:

1. **Core Layer**: Translation memory, project management, document processing
2. **Services Layer**: Machine translation, terminology, QA (planned)
3. **UI Layer**: JavaFX-based user interface
4. **Plugin System**: Extensible plugin architecture (planned)

See [ARCHITECTURE.md](ARCHITECTURE.md) for detailed architecture documentation.

## Configuration

Edit `src/main/resources/application.properties` to configure:

- Database settings
- Translation memory parameters
- UI preferences
- Performance tuning
- Logging levels

## Usage

### Creating a New Project

1. Launch TranslationPro
2. Click "New Project" or File > New Project
3. Enter project details:
   - Project name
   - Source language
   - Target language(s)
   - Project location
4. Click "Create"

### Importing Source Files

1. Open a project
2. Click "Import Files" or Project > Import Source Files
3. Select files to translate
4. Files will be segmented and ready for translation

### Translating

1. Navigate through segments using arrow keys or mouse
2. Translation memory matches appear automatically
3. Enter or edit translation in the target field
4. Press Enter to confirm and move to next segment
5. Project auto-saves periodically

### Exporting Translations

1. Project > Export Translated Documents
2. Select target language
3. Choose output location
4. Translated files are generated

## Advantages Over OmegaT and Wordfast Pro

### vs OmegaT

- Modern JavaFX UI (vs Swing)
- Database-backed TM (vs file-based)
- Better performance with large TMs
- Enhanced fuzzy matching algorithms
- Built-in plugin architecture
- Modern Java 17 (vs Java 8)

### vs Wordfast Pro 3

- Open source and free
- More extensible architecture
- Better Windows 11 integration
- Modern UI/UX
- No licensing restrictions
- Active development

## Roadmap

### Phase 1 (Current)
- ✅ Core architecture and foundation
- ✅ Translation memory engine
- ✅ Project management
- ✅ Basic document processing
- ✅ Basic UI framework
- ⏳ TMX import/export
- ⏳ Complete editor interface

### Phase 2 (Q1 2025)
- Machine translation integration
- Quality assurance system
- Terminology management
- Advanced file format support
- Plugin system implementation

### Phase 3 (Q2 2025)
- Advanced UI features
- Real-time collaboration
- AI-powered translation assistance
- Cloud sync (optional)
- Marketplace for plugins

## Contributing

Contributions are welcome! Please feel free to submit pull requests.

## License

This project is open source. License to be determined.

## Support

For issues, questions, or feature requests, please open an issue on GitHub.

## Acknowledgments

- Based on principles from [OmegaT](https://omegat.org/)
- Inspired by professional CAT tools like Wordfast Pro, SDL Trados, and MemoQ
- Built with modern Java and JavaFX technologies

## Contact

For more information, please visit the [GitHub repository](https://github.com/MikeZhang110/translationpro).

---

**TranslationPro** - Professional translation tools for the modern translator
