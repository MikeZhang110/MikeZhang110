package com.translationpro;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.translationpro.core.CoreModule;
import com.translationpro.ui.MainWindow;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Main application entry point for TranslationPro.
 *
 * TranslationPro is an advanced Computer-Assisted Translation tool that combines
 * the open-source philosophy of OmegaT with enterprise-grade features.
 */
public class TranslationProApp extends Application {

    private static final Logger logger = LoggerFactory.getLogger(TranslationProApp.class);
    private static Injector injector;
    private static Properties appProperties;

    public static void main(String[] args) {
        // Load configuration
        loadProperties();

        // Initialize logging
        logger.info("Starting TranslationPro v{}", appProperties.getProperty("app.version"));

        // Launch JavaFX application
        launch(args);
    }

    @Override
    public void init() throws Exception {
        super.init();

        // Initialize dependency injection
        logger.info("Initializing dependency injection container");
        injector = Guice.createInjector(new CoreModule());

        // Initialize application directories
        initializeDirectories();

        // Load plugins
        logger.info("Loading plugins");
        // PluginManager will be initialized here
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            logger.info("Starting UI");

            // Get main window from DI container
            MainWindow mainWindow = injector.getInstance(MainWindow.class);

            // Create scene
            Scene scene = new Scene(mainWindow.getRoot(), 1400, 900);

            // Load stylesheets
            String theme = appProperties.getProperty("ui.theme", "modern-light");
            scene.getStylesheets().add(
                getClass().getResource("/styles/" + theme + ".css").toExternalForm()
            );

            // Configure primary stage
            primaryStage.setTitle("TranslationPro - Professional CAT Tool");
            primaryStage.setScene(scene);
            primaryStage.setMaximized(true);

            // Show window
            primaryStage.show();

            logger.info("Application started successfully");

        } catch (Exception e) {
            logger.error("Failed to start application", e);
            throw new RuntimeException("Failed to start TranslationPro", e);
        }
    }

    @Override
    public void stop() throws Exception {
        logger.info("Shutting down TranslationPro");

        // Cleanup resources
        // Close database connections, save preferences, etc.

        super.stop();
        logger.info("Application stopped");
    }

    /**
     * Load application properties from resources.
     */
    private static void loadProperties() {
        appProperties = new Properties();
        try (InputStream input = TranslationProApp.class
                .getResourceAsStream("/application.properties")) {
            if (input != null) {
                appProperties.load(input);
            }
        } catch (IOException e) {
            logger.warn("Could not load application.properties, using defaults", e);
        }
    }

    /**
     * Initialize application directories.
     */
    private void initializeDirectories() {
        // Create necessary directories
        String[] directories = {
            "./data",
            "./data/tm",
            "./data/projects",
            "./data/glossaries",
            "./logs",
            "./plugins"
        };

        for (String dir : directories) {
            java.io.File directory = new java.io.File(dir);
            if (!directory.exists()) {
                boolean created = directory.mkdirs();
                if (created) {
                    logger.debug("Created directory: {}", dir);
                }
            }
        }
    }

    /**
     * Get the dependency injection container.
     */
    public static Injector getInjector() {
        return injector;
    }

    /**
     * Get application properties.
     */
    public static Properties getProperties() {
        return appProperties;
    }
}
