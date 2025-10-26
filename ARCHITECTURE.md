# TranslationPro - Advanced CAT Tool Architecture

## Vision
A next-generation Computer-Assisted Translation tool that combines the open-source philosophy of OmegaT with enterprise-grade features, modern UI/UX, and extensibility that surpasses both OmegaT and Wordfast Pro.

## Core Design Principles

1. **Plugin-First Architecture**: Every major feature is a plugin, making the system infinitely extensible
2. **Performance**: Native database for TM, lazy loading, multi-threading
3. **Modern Stack**: Java 17+, JavaFX for UI, reactive programming patterns
4. **Local-First**: Standalone Windows 11 app with optional cloud sync
5. **Professional Grade**: Enterprise QA, terminology management, project management
6. **AI-Ready**: Built-in framework for ML/AI integrations

## Technology Stack

### Core
- **Language**: Java 17+ (LTS with modern features)
- **Build System**: Gradle 8.x with Kotlin DSL
- **Database**: H2 (embedded) for translation memory and terminology
- **UI Framework**: JavaFX 21+ with modern styling
- **DI Container**: Google Guice for dependency injection
- **Event Bus**: Guava EventBus for loose coupling

### Libraries
- **TMX Support**: Custom TMX 1.4b parser/writer
- **File Formats**: Apache Tika + custom filters (XLIFF, SDLXLIFF, IDML, etc.)
- **Segmentation**: ICU4J for Unicode text segmentation
- **Fuzzy Matching**: Custom Levenshtein + BK-tree indexing
- **Spellcheck**: Hunspell JNA wrapper
- **MT Integration**: REST clients for Google, DeepL, Azure, OpenAI
- **Terminology**: Custom terminology extraction algorithms

## Architecture Layers

### 1. Core Layer (`com.translationpro.core`)

#### Translation Memory Engine
```
core.tm
├── TranslationMemoryService (interface)
├── H2TranslationMemoryImpl (fast database backend)
├── TMXImporter/Exporter (TMX 1.4b support)
├── FuzzyMatcher (advanced matching with context)
├── Segmentation (ICU-based with custom rules)
└── AlignmentService (bilingual document alignment)
```

**Enhanced Features**:
- Multi-level fuzzy matching (character, word, phrase, context)
- Context-aware matching (previous/next segment consideration)
- Subsegment matching for technical terms
- Quality scoring for matches
- Batch TM operations
- Incremental indexing

#### Project Management
```
core.project
├── ProjectService
├── ProjectStructure (flexible folder organization)
├── FileManager (watch service for auto-reload)
├── MetadataManager (project settings, statistics)
└── VersionControl (built-in git integration)
```

#### Document Processing
```
core.document
├── DocumentParser (pluggable parsers)
├── FilterManager (file format filters)
├── SegmentStore (efficient segment storage)
├── TagManager (inline tag handling)
└── BilingualFileGenerator (XLIFF export)
```

### 2. Service Layer (`com.translationpro.services`)

#### Machine Translation
```
services.mt
├── MTProviderRegistry (plugin registry)
├── MTAdapter (unified interface)
├── Providers:
│   ├── GoogleTranslateProvider
│   ├── DeepLProvider
│   ├── AzureTranslatorProvider
│   ├── OpenAIProvider (GPT-4 for translation)
│   └── LocalMTProvider (offline models)
├── MTQualityEstimator
└── BatchMTProcessor
```

#### Terminology Management
```
services.terminology
├── TermbaseService
├── TermExtractor (automatic extraction)
├── TermValidator (consistency checking)
├── GlossaryManager (multi-glossary support)
└── TermRecognizer (in-context highlighting)
```

#### Quality Assurance
```
services.qa
├── QACheckEngine
├── Checks:
│   ├── ConsistencyCheck
│   ├── NumbersCheck
│   ├── PunctuationCheck
│   ├── TerminologyCheck
│   ├── LengthCheck
│   ├── SpellCheck
│   ├── GrammarCheck (LanguageTool integration)
│   ├── FormatCheck (tag validation)
│   └── CustomRegexCheck
├── IssueManager
└── QAReportGenerator
```

### 3. UI Layer (`com.translationpro.ui`)

#### Modern JavaFX Interface
```
ui
├── MainWindow (ribbon-style toolbar)
├── EditorPane
│   ├── SegmentEditor (inline editing with rich text)
│   ├── MatchesPanel (TM/MT matches side-by-side)
│   ├── GlossaryPanel (contextual terms)
│   └── CommentsPanel (translator notes)
├── ProjectBrowser (tree view with filters)
├── SearchDialog (advanced search & replace)
├── PreferencesDialog (categorized settings)
├── StatisticsView (real-time analytics)
└── DashboardView (project overview)
```

**UI Innovations**:
- Split-screen editing (source/target side-by-side)
- Customizable layouts (save/restore workspaces)
- Dark/light themes with custom styling
- Keyboard-centric workflow (vim-like shortcuts)
- Real-time collaboration indicators
- Inline QA highlighting
- Visual tag editors

### 4. Plugin System (`com.translationpro.plugin`)

```
plugin
├── PluginManager
├── PluginClassLoader (isolated classloaders)
├── PluginAPI
│   ├── IDocumentFilter (custom file formats)
│   ├── IMTProvider (machine translation)
│   ├── IQACheck (custom QA rules)
│   ├── ITermExtractor (terminology extraction)
│   ├── IUIExtension (UI plugins)
│   └── IWorkflowAction (automation)
├── PluginRepository (plugin discovery)
└── PluginSandbox (security constraints)
```

**Plugin Capabilities**:
- Hot reload without restart
- Versioned plugin API
- Plugin dependency resolution
- Marketplace integration
- Sandboxed execution

### 5. Integration Layer (`com.translationpro.integration`)

```
integration
├── ImportExport
│   ├── TMXImportExport
│   ├── XLIFFImportExport
│   ├── TradosPackageHandler
│   ├── MemoQPackageHandler
│   └── WordfastTMHandler
├── CloudSync (optional)
│   ├── CloudProvider (interface)
│   ├── GoogleDriveSync
│   └── OneDriveSync
└── API
    ├── RESTServer (optional local API)
    └── ScriptingEngine (automation scripts)
```

## Database Schema

### Translation Memory (H2 Database)

```sql
-- Translation Units
CREATE TABLE translation_units (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    source_text TEXT NOT NULL,
    target_text TEXT NOT NULL,
    source_lang VARCHAR(10) NOT NULL,
    target_lang VARCHAR(10) NOT NULL,
    source_hash VARCHAR(64) NOT NULL, -- for fast lookup
    creation_date TIMESTAMP NOT NULL,
    modification_date TIMESTAMP,
    creator VARCHAR(255),
    project_name VARCHAR(255),
    subject_field VARCHAR(100),
    client VARCHAR(255),
    quality_score TINYINT DEFAULT 0,
    usage_count INT DEFAULT 0,
    INDEX idx_hash (source_hash),
    INDEX idx_langs (source_lang, target_lang),
    INDEX idx_date (creation_date)
);

-- Segments (for context)
CREATE TABLE segments (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    tu_id BIGINT NOT NULL,
    previous_source TEXT,
    next_source TEXT,
    document_name VARCHAR(255),
    segment_number INT,
    FOREIGN KEY (tu_id) REFERENCES translation_units(id) ON DELETE CASCADE
);

-- Terminology
CREATE TABLE terms (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    source_term VARCHAR(255) NOT NULL,
    target_term VARCHAR(255) NOT NULL,
    source_lang VARCHAR(10) NOT NULL,
    target_lang VARCHAR(10) NOT NULL,
    definition TEXT,
    context TEXT,
    subject_field VARCHAR(100),
    forbidden BOOLEAN DEFAULT FALSE,
    INDEX idx_source (source_term),
    INDEX idx_langs (source_lang, target_lang)
);

-- Projects
CREATE TABLE projects (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL UNIQUE,
    source_lang VARCHAR(10) NOT NULL,
    target_langs VARCHAR(255) NOT NULL,
    creation_date TIMESTAMP NOT NULL,
    settings TEXT, -- JSON configuration
    statistics TEXT -- JSON statistics
);
```

## Key Features Beyond OmegaT/Wordfast

### 1. Advanced Fuzzy Matching
- Context-aware matching (consider surrounding segments)
- Subsegment matching for partial reuse
- Intelligent tag handling (position-aware)
- Machine learning similarity scoring

### 2. Real-Time Collaboration (Local Network)
- Share project over LAN
- Live segment locking
- Translator presence indicators
- Conflict resolution

### 3. Smart Terminology
- Automatic term extraction from TM
- Term consistency checking across project
- Forbidden term detection
- Multilingual term bases

### 4. Advanced QA
- 15+ built-in QA checks
- Custom regex-based checks
- Grammar checking (LanguageTool)
- Style guide enforcement
- Automated fix suggestions

### 5. AI Integration
- GPT-4 for context-aware translation
- Quality estimation for MT output
- Automatic post-editing suggestions
- Translation confidence scoring

### 6. Project Intelligence
- Automatic subject field detection
- Project statistics and analytics
- Translation speed tracking
- Cost estimation

### 7. Modern UX
- Ribbon-style interface
- Customizable keyboard shortcuts
- Dark mode with multiple themes
- Dockable panels
- Multi-monitor support
- Touch screen support

### 8. Performance
- Lazy loading for large projects
- Incremental TM indexing
- Multi-threaded processing
- Memory-mapped file handling
- Sub-second TM search (even for millions of TUs)

## File Format Support

### Native Support (Priority 1)
- XLIFF 1.2, 2.0, 2.1
- TMX 1.4b
- Plain text (.txt)
- HTML/XHTML
- XML (generic)
- Properties files
- PO (gettext)
- RESX (.NET resources)
- JSON (i18n)

### Via Filters (Priority 2)
- Microsoft Office (DOCX, XLSX, PPTX)
- OpenDocument (ODT, ODS, ODP)
- Adobe InDesign (IDML)
- SDL Trados (SDLXLIFF, SDLPPX)
- Wordfast (TXML)
- MemoQ (MQXLIFF)
- PDF (text extraction)
- Markdown
- YAML

### Plugin Extensions (Priority 3)
- CAD file formats
- Subtitle formats (SRT, VTT)
- Software strings (iOS, Android)
- Database content
- CMS connectors

## Build and Packaging

### Gradle Build Structure
```
build.gradle.kts
├── Java 17 compilation
├── JavaFX plugin
├── Shadow JAR (fat JAR with dependencies)
├── JPackage integration (native Windows installer)
├── Test coverage (JaCoCo)
└── Plugin development SDK
```

### Distribution
- Standalone Windows installer (.msi)
- Portable ZIP package
- Auto-update mechanism
- Plugin marketplace integration

## Performance Targets

- Application startup: < 2 seconds
- Open project (10,000 segments): < 3 seconds
- TM search (1M TUs): < 100ms
- Segment switch: < 50ms
- Auto-save: < 500ms (background)
- Memory usage: < 1GB for typical projects

## Extensibility

Every major component implements an interface and can be:
1. **Replaced**: Swap implementations via configuration
2. **Extended**: Plugins can add new implementations
3. **Composed**: Chain multiple implementations

Example: MT providers are plugins, so users can add any MT service without modifying core code.

## Security & Privacy

- Local-first: all data stays on user's machine
- Optional cloud sync (encrypted)
- No telemetry by default
- Plugin sandboxing
- Secure credential storage (Windows Credential Manager)

## Future Roadmap

### Phase 2 (Post-MVP)
- Web-based UI (keep Java backend)
- Mobile companion app
- Advanced statistical analysis
- Neural MT training integration
- Voice transcription support
- AR/VR translation interface

### Phase 3
- Cloud-native version (optional)
- Marketplace for plugins/resources
- Translation crowdsourcing
- Blockchain-based translation certification
