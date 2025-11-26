package com.translationpro.core.project;

import java.util.List;

/**
 * Service interface for project management operations.
 */
public interface ProjectService {

    /**
     * Create a new translation project.
     *
     * @param project the project to create
     * @return the created project
     */
    Project createProject(Project project);

    /**
     * Load an existing project from disk.
     *
     * @param projectPath path to the project directory
     * @return the loaded project
     */
    Project loadProject(String projectPath);

    /**
     * Save the current project.
     *
     * @param project the project to save
     */
    void saveProject(Project project);

    /**
     * Close the current project.
     */
    void closeProject();

    /**
     * Get the currently opened project.
     *
     * @return the current project, or null if no project is open
     */
    Project getCurrentProject();

    /**
     * Get a list of recently opened projects.
     *
     * @return list of recent project paths
     */
    List<String> getRecentProjects();

    /**
     * Delete a project from disk.
     *
     * @param projectPath path to the project directory
     */
    void deleteProject(String projectPath);

    /**
     * Check if a project exists at the given path.
     *
     * @param projectPath path to check
     * @return true if a valid project exists
     */
    boolean projectExists(String projectPath);

    /**
     * Get project structure information.
     *
     * @param project the project
     * @return project structure details
     */
    ProjectStructure getProjectStructure(Project project);
}
