package com.translationpro.ui;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.translationpro.core.document.DocumentService;
import com.translationpro.core.project.ProjectService;
import com.translationpro.core.tm.TranslationMemoryService;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main window of the application.
 */
@Singleton
public class MainWindow {

    private static final Logger logger = LoggerFactory.getLogger(MainWindow.class);

    private final ProjectService projectService;
    private final DocumentService documentService;
    private final TranslationMemoryService tmService;

    private final BorderPane root;
    private final MenuBar menuBar;
    private final ToolBar toolBar;
    private final TabPane editorTabPane;
    private final VBox statusBar;

    @Inject
    public MainWindow(ProjectService projectService,
                      DocumentService documentService,
                      TranslationMemoryService tmService) {
        this.projectService = projectService;
        this.documentService = documentService;
        this.tmService = tmService;

        this.root = new BorderPane();
        this.menuBar = createMenuBar();
        this.toolBar = createToolBar();
        this.editorTabPane = createEditorPane();
        this.statusBar = createStatusBar();

        setupLayout();
    }

    private void setupLayout() {
        // Set up main layout
        VBox topContainer = new VBox(menuBar, toolBar);
        root.setTop(topContainer);
        root.setCenter(editorTabPane);
        root.setBottom(statusBar);
    }

    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();

        // File menu
        Menu fileMenu = new Menu("File");
        MenuItem newProject = new MenuItem("New Project...");
        newProject.setOnAction(e -> handleNewProject());

        MenuItem openProject = new MenuItem("Open Project...");
        openProject.setOnAction(e -> handleOpenProject());

        MenuItem saveProject = new MenuItem("Save Project");
        saveProject.setOnAction(e -> handleSaveProject());

        MenuItem closeProject = new MenuItem("Close Project");
        closeProject.setOnAction(e -> handleCloseProject());

        SeparatorMenuItem separator1 = new SeparatorMenuItem();

        MenuItem exit = new MenuItem("Exit");
        exit.setOnAction(e -> handleExit());

        fileMenu.getItems().addAll(newProject, openProject, saveProject, closeProject, separator1, exit);

        // Edit menu
        Menu editMenu = new Menu("Edit");
        MenuItem undo = new MenuItem("Undo");
        MenuItem redo = new MenuItem("Redo");
        MenuItem cut = new MenuItem("Cut");
        MenuItem copy = new MenuItem("Copy");
        MenuItem paste = new MenuItem("Paste");
        editMenu.getItems().addAll(undo, redo, new SeparatorMenuItem(), cut, copy, paste);

        // Project menu
        Menu projectMenu = new Menu("Project");
        MenuItem importFiles = new MenuItem("Import Source Files...");
        importFiles.setOnAction(e -> handleImportFiles());

        MenuItem projectSettings = new MenuItem("Project Settings...");
        projectSettings.setOnAction(e -> handleProjectSettings());

        projectMenu.getItems().addAll(importFiles, projectSettings);

        // Tools menu
        Menu toolsMenu = new Menu("Tools");
        MenuItem tmManager = new MenuItem("Translation Memory Manager...");
        tmManager.setOnAction(e -> handleTMManager());

        MenuItem glossaryManager = new MenuItem("Glossary Manager...");
        glossaryManager.setOnAction(e -> handleGlossaryManager());

        MenuItem qaChecks = new MenuItem("Run QA Checks");
        qaChecks.setOnAction(e -> handleQAChecks());

        toolsMenu.getItems().addAll(tmManager, glossaryManager, new SeparatorMenuItem(), qaChecks);

        // Help menu
        Menu helpMenu = new Menu("Help");
        MenuItem documentation = new MenuItem("Documentation");
        MenuItem about = new MenuItem("About TranslationPro");
        about.setOnAction(e -> handleAbout());

        helpMenu.getItems().addAll(documentation, new SeparatorMenuItem(), about);

        menuBar.getMenus().addAll(fileMenu, editMenu, projectMenu, toolsMenu, helpMenu);
        return menuBar;
    }

    private ToolBar createToolBar() {
        ToolBar toolBar = new ToolBar();

        Button newProjectBtn = new Button("New Project");
        newProjectBtn.setOnAction(e -> handleNewProject());

        Button openProjectBtn = new Button("Open Project");
        openProjectBtn.setOnAction(e -> handleOpenProject());

        Button saveBtn = new Button("Save");
        saveBtn.setOnAction(e -> handleSaveProject());

        Separator sep1 = new Separator();

        Button importBtn = new Button("Import Files");
        importBtn.setOnAction(e -> handleImportFiles());

        Separator sep2 = new Separator();

        Button tmBtn = new Button("TM Manager");
        tmBtn.setOnAction(e -> handleTMManager());

        Button glossaryBtn = new Button("Glossary");
        glossaryBtn.setOnAction(e -> handleGlossaryManager());

        toolBar.getItems().addAll(
                newProjectBtn, openProjectBtn, saveBtn, sep1,
                importBtn, sep2,
                tmBtn, glossaryBtn
        );

        return toolBar;
    }

    private TabPane createEditorPane() {
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);

        // Welcome tab
        Tab welcomeTab = new Tab("Welcome");
        welcomeTab.setClosable(false);
        welcomeTab.setContent(createWelcomePane());

        tabPane.getTabs().add(welcomeTab);
        return tabPane;
    }

    private VBox createWelcomePane() {
        VBox welcomePane = new VBox(20);
        welcomePane.setStyle("-fx-padding: 40; -fx-alignment: center;");

        Label titleLabel = new Label("Welcome to TranslationPro");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Label subtitleLabel = new Label("Professional Computer-Assisted Translation Tool");
        subtitleLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: gray;");

        Button newProjectBtn = new Button("Create New Project");
        newProjectBtn.setPrefWidth(200);
        newProjectBtn.setOnAction(e -> handleNewProject());

        Button openProjectBtn = new Button("Open Existing Project");
        openProjectBtn.setPrefWidth(200);
        openProjectBtn.setOnAction(e -> handleOpenProject());

        // Recent projects list
        Label recentLabel = new Label("Recent Projects:");
        ListView<String> recentList = new ListView<>();
        recentList.setPrefHeight(200);
        recentList.getItems().addAll(projectService.getRecentProjects());

        welcomePane.getChildren().addAll(
                titleLabel, subtitleLabel,
                newProjectBtn, openProjectBtn,
                recentLabel, recentList
        );

        return welcomePane;
    }

    private VBox createStatusBar() {
        VBox statusBar = new VBox();
        statusBar.setStyle("-fx-padding: 5; -fx-background-color: #f0f0f0;");

        Label statusLabel = new Label("Ready");
        statusBar.getChildren().add(statusLabel);

        return statusBar;
    }

    // Event handlers

    private void handleNewProject() {
        logger.info("New project requested");
        // TODO: Show new project dialog
        showInfo("New Project", "New project dialog not yet implemented");
    }

    private void handleOpenProject() {
        logger.info("Open project requested");
        // TODO: Show file chooser
        showInfo("Open Project", "Open project dialog not yet implemented");
    }

    private void handleSaveProject() {
        if (projectService.getCurrentProject() != null) {
            projectService.saveProject(projectService.getCurrentProject());
            showInfo("Save", "Project saved successfully");
        } else {
            showWarning("Save", "No project is currently open");
        }
    }

    private void handleCloseProject() {
        projectService.closeProject();
        logger.info("Project closed");
    }

    private void handleExit() {
        logger.info("Exit requested");
        System.exit(0);
    }

    private void handleImportFiles() {
        logger.info("Import files requested");
        showInfo("Import Files", "Import files dialog not yet implemented");
    }

    private void handleProjectSettings() {
        logger.info("Project settings requested");
        showInfo("Project Settings", "Project settings dialog not yet implemented");
    }

    private void handleTMManager() {
        logger.info("TM manager requested");
        showInfo("TM Manager", "Translation Memory Manager not yet implemented");
    }

    private void handleGlossaryManager() {
        logger.info("Glossary manager requested");
        showInfo("Glossary Manager", "Glossary Manager not yet implemented");
    }

    private void handleQAChecks() {
        logger.info("QA checks requested");
        showInfo("QA Checks", "Quality Assurance Checks not yet implemented");
    }

    private void handleAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About TranslationPro");
        alert.setHeaderText("TranslationPro v1.0.0");
        alert.setContentText(
                "Professional Computer-Assisted Translation Tool\n\n" +
                "Based on OmegaT principles with modern architecture\n" +
                "Built with Java 17 and JavaFX\n\n" +
                "© 2024 TranslationPro"
        );
        alert.showAndWait();
    }

    // Utility methods

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showWarning(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public Parent getRoot() {
        return root;
    }
}
