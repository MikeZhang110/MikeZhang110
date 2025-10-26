package com.translationpro.core.project;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;
import java.util.stream.Stream;

/**
 * Implementation of the project service.
 */
@Singleton
public class ProjectServiceImpl implements ProjectService {

    private static final Logger logger = LoggerFactory.getLogger(ProjectServiceImpl.class);
    private static final String RECENT_PROJECTS_KEY = "recentProjects";
    private static final int MAX_RECENT_PROJECTS = 10;

    private final ObjectMapper objectMapper;
    private final Preferences preferences;
    private Project currentProject;

    public ProjectServiceImpl() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.preferences = Preferences.userNodeForPackage(ProjectServiceImpl.class);
    }

    @Override
    public Project createProject(Project project) {
        try {
            Path projectPath = Paths.get(project.getProjectPath());

            // Create project directory structure
            Files.createDirectories(projectPath);
            Files.createDirectories(projectPath.resolve("source"));
            Files.createDirectories(projectPath.resolve("target"));
            Files.createDirectories(projectPath.resolve("tm"));
            Files.createDirectories(projectPath.resolve("glossary"));
            Files.createDirectories(projectPath.resolve("dictionary"));

            // Save project configuration
            saveProject(project);

            // Add to recent projects
            addToRecentProjects(project.getProjectPath());

            logger.info("Created project: {}", project.getName());
            this.currentProject = project;

            return project;

        } catch (IOException e) {
            logger.error("Failed to create project", e);
            throw new RuntimeException("Failed to create project", e);
        }
    }

    @Override
    public Project loadProject(String projectPath) {
        try {
            Path path = Paths.get(projectPath);
            Path projectFile = path.resolve("project.json");

            if (!Files.exists(projectFile)) {
                throw new IllegalArgumentException("Project file not found: " + projectFile);
            }

            // Load project from JSON
            Project project = objectMapper.readValue(projectFile.toFile(), Project.class);
            project.setProjectPath(projectPath);

            // Add to recent projects
            addToRecentProjects(projectPath);

            logger.info("Loaded project: {}", project.getName());
            this.currentProject = project;

            return project;

        } catch (IOException e) {
            logger.error("Failed to load project", e);
            throw new RuntimeException("Failed to load project", e);
        }
    }

    @Override
    public void saveProject(Project project) {
        try {
            Path projectFile = Paths.get(project.getProjectPath()).resolve("project.json");

            // Save project as JSON
            objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValue(projectFile.toFile(), project);

            logger.debug("Saved project: {}", project.getName());

        } catch (IOException e) {
            logger.error("Failed to save project", e);
            throw new RuntimeException("Failed to save project", e);
        }
    }

    @Override
    public void closeProject() {
        if (currentProject != null) {
            saveProject(currentProject);
            logger.info("Closed project: {}", currentProject.getName());
            this.currentProject = null;
        }
    }

    @Override
    public Project getCurrentProject() {
        return currentProject;
    }

    @Override
    public List<String> getRecentProjects() {
        String recentProjectsStr = preferences.get(RECENT_PROJECTS_KEY, "");
        List<String> recentProjects = new ArrayList<>();

        if (!recentProjectsStr.isEmpty()) {
            String[] projects = recentProjectsStr.split(";");
            for (String project : projects) {
                if (!project.isEmpty() && new File(project).exists()) {
                    recentProjects.add(project);
                }
            }
        }

        return recentProjects;
    }

    private void addToRecentProjects(String projectPath) {
        List<String> recentProjects = new ArrayList<>(getRecentProjects());

        // Remove if already exists
        recentProjects.remove(projectPath);

        // Add to front
        recentProjects.add(0, projectPath);

        // Limit size
        if (recentProjects.size() > MAX_RECENT_PROJECTS) {
            recentProjects = recentProjects.subList(0, MAX_RECENT_PROJECTS);
        }

        // Save
        String recentProjectsStr = String.join(";", recentProjects);
        preferences.put(RECENT_PROJECTS_KEY, recentProjectsStr);
    }

    @Override
    public void deleteProject(String projectPath) {
        try {
            Path path = Paths.get(projectPath);
            if (Files.exists(path)) {
                // Delete project directory recursively
                try (Stream<Path> walk = Files.walk(path)) {
                    walk.sorted((a, b) -> -a.compareTo(b))
                        .forEach(p -> {
                            try {
                                Files.delete(p);
                            } catch (IOException e) {
                                logger.error("Failed to delete file: {}", p, e);
                            }
                        });
                }

                // Remove from recent projects
                List<String> recentProjects = new ArrayList<>(getRecentProjects());
                recentProjects.remove(projectPath);
                String recentProjectsStr = String.join(";", recentProjects);
                preferences.put(RECENT_PROJECTS_KEY, recentProjectsStr);

                logger.info("Deleted project: {}", projectPath);
            }

        } catch (IOException e) {
            logger.error("Failed to delete project", e);
            throw new RuntimeException("Failed to delete project", e);
        }
    }

    @Override
    public boolean projectExists(String projectPath) {
        Path path = Paths.get(projectPath);
        Path projectFile = path.resolve("project.json");
        return Files.exists(projectFile);
    }

    @Override
    public ProjectStructure getProjectStructure(Project project) {
        Path rootPath = Paths.get(project.getProjectPath());
        ProjectStructure structure = new ProjectStructure(rootPath);

        // Find all source files
        try (Stream<Path> files = Files.walk(structure.getSourcePath())) {
            files.filter(Files::isRegularFile)
                 .forEach(file -> structure.addSourceFile(
                         structure.getSourcePath().relativize(file).toString()
                 ));
        } catch (IOException e) {
            logger.error("Failed to scan source files", e);
        }

        return structure;
    }
}
