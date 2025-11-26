package com.translationpro.core.project;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the directory structure of a translation project.
 */
public class ProjectStructure {

    private Path rootPath;
    private Path sourcePath;
    private Path targetPath;
    private Path tmPath;
    private Path glossaryPath;
    private Path dictionaryPath;
    private List<String> sourceFiles;

    public ProjectStructure(Path rootPath) {
        this.rootPath = rootPath;
        this.sourcePath = rootPath.resolve("source");
        this.targetPath = rootPath.resolve("target");
        this.tmPath = rootPath.resolve("tm");
        this.glossaryPath = rootPath.resolve("glossary");
        this.dictionaryPath = rootPath.resolve("dictionary");
        this.sourceFiles = new ArrayList<>();
    }

    public Path getRootPath() {
        return rootPath;
    }

    public Path getSourcePath() {
        return sourcePath;
    }

    public Path getTargetPath() {
        return targetPath;
    }

    public Path getTmPath() {
        return tmPath;
    }

    public Path getGlossaryPath() {
        return glossaryPath;
    }

    public Path getDictionaryPath() {
        return dictionaryPath;
    }

    public List<String> getSourceFiles() {
        return sourceFiles;
    }

    public void addSourceFile(String fileName) {
        this.sourceFiles.add(fileName);
    }

    public Path getProjectFilePath() {
        return rootPath.resolve("project.json");
    }

    @Override
    public String toString() {
        return "ProjectStructure[" + rootPath + "]";
    }
}
