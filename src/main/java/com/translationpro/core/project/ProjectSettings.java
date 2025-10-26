package com.translationpro.core.project;

/**
 * Project-specific settings and configuration.
 */
public class ProjectSettings {

    private boolean autoSave = true;
    private int autoSaveInterval = 30000; // milliseconds
    private int fuzzyMatchThreshold = 70;
    private boolean useContextMatching = true;
    private boolean enableMachineTranslation = true;
    private boolean enableQualityChecks = true;
    private String segmentationRules = "default";
    private String defaultTermbase;
    private String client;
    private String subjectField;

    public ProjectSettings() {
    }

    // Getters and setters

    public boolean isAutoSave() {
        return autoSave;
    }

    public void setAutoSave(boolean autoSave) {
        this.autoSave = autoSave;
    }

    public int getAutoSaveInterval() {
        return autoSaveInterval;
    }

    public void setAutoSaveInterval(int autoSaveInterval) {
        this.autoSaveInterval = autoSaveInterval;
    }

    public int getFuzzyMatchThreshold() {
        return fuzzyMatchThreshold;
    }

    public void setFuzzyMatchThreshold(int fuzzyMatchThreshold) {
        this.fuzzyMatchThreshold = fuzzyMatchThreshold;
    }

    public boolean isUseContextMatching() {
        return useContextMatching;
    }

    public void setUseContextMatching(boolean useContextMatching) {
        this.useContextMatching = useContextMatching;
    }

    public boolean isEnableMachineTranslation() {
        return enableMachineTranslation;
    }

    public void setEnableMachineTranslation(boolean enableMachineTranslation) {
        this.enableMachineTranslation = enableMachineTranslation;
    }

    public boolean isEnableQualityChecks() {
        return enableQualityChecks;
    }

    public void setEnableQualityChecks(boolean enableQualityChecks) {
        this.enableQualityChecks = enableQualityChecks;
    }

    public String getSegmentationRules() {
        return segmentationRules;
    }

    public void setSegmentationRules(String segmentationRules) {
        this.segmentationRules = segmentationRules;
    }

    public String getDefaultTermbase() {
        return defaultTermbase;
    }

    public void setDefaultTermbase(String defaultTermbase) {
        this.defaultTermbase = defaultTermbase;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getSubjectField() {
        return subjectField;
    }

    public void setSubjectField(String subjectField) {
        this.subjectField = subjectField;
    }
}
