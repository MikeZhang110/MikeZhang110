package com.translationpro.core.project;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a translation project.
 */
public class Project {

    private Long id;
    private String name;
    private String sourceLang;
    private List<String> targetLangs;
    private String projectPath;
    private LocalDateTime creationDate;
    private LocalDateTime lastModifiedDate;
    private ProjectSettings settings;
    private ProjectStatistics statistics;

    public Project() {
        this.targetLangs = new ArrayList<>();
        this.creationDate = LocalDateTime.now();
        this.settings = new ProjectSettings();
        this.statistics = new ProjectStatistics();
    }

    public Project(String name, String sourceLang, List<String> targetLangs, String projectPath) {
        this();
        this.name = name;
        this.sourceLang = sourceLang;
        this.targetLangs = targetLangs;
        this.projectPath = projectPath;
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.lastModifiedDate = LocalDateTime.now();
    }

    public String getSourceLang() {
        return sourceLang;
    }

    public void setSourceLang(String sourceLang) {
        this.sourceLang = sourceLang;
    }

    public List<String> getTargetLangs() {
        return targetLangs;
    }

    public void setTargetLangs(List<String> targetLangs) {
        this.targetLangs = targetLangs;
    }

    public void addTargetLang(String targetLang) {
        if (!this.targetLangs.contains(targetLang)) {
            this.targetLangs.add(targetLang);
        }
    }

    public String getProjectPath() {
        return projectPath;
    }

    public void setProjectPath(String projectPath) {
        this.projectPath = projectPath;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public ProjectSettings getSettings() {
        return settings;
    }

    public void setSettings(ProjectSettings settings) {
        this.settings = settings;
    }

    public ProjectStatistics getStatistics() {
        return statistics;
    }

    public void setStatistics(ProjectStatistics statistics) {
        this.statistics = statistics;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;
        return Objects.equals(name, project.name) &&
               Objects.equals(projectPath, project.projectPath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, projectPath);
    }

    @Override
    public String toString() {
        return String.format("Project[%s: %s -> %s]", name, sourceLang, targetLangs);
    }
}
