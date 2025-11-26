package com.translationpro.core.tm;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a translation unit (source + target pair) in the translation memory.
 */
public class TranslationUnit {

    private Long id;
    private String sourceText;
    private String targetText;
    private String sourceLang;
    private String targetLang;
    private String sourceHash;
    private LocalDateTime creationDate;
    private LocalDateTime modificationDate;
    private String creator;
    private String projectName;
    private String subjectField;
    private String client;
    private int qualityScore;
    private int usageCount;

    // Context information
    private String previousSource;
    private String nextSource;
    private String documentName;
    private int segmentNumber;

    public TranslationUnit() {
        this.creationDate = LocalDateTime.now();
        this.qualityScore = 0;
        this.usageCount = 0;
    }

    public TranslationUnit(String sourceText, String targetText, String sourceLang, String targetLang) {
        this();
        this.sourceText = sourceText;
        this.targetText = targetText;
        this.sourceLang = sourceLang;
        this.targetLang = targetLang;
        this.sourceHash = calculateHash(sourceText);
    }

    /**
     * Calculate hash for fast lookup.
     */
    private String calculateHash(String text) {
        // Simple hash for now, can be improved with MD5/SHA
        return Integer.toHexString(text.hashCode());
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSourceText() {
        return sourceText;
    }

    public void setSourceText(String sourceText) {
        this.sourceText = sourceText;
        this.sourceHash = calculateHash(sourceText);
    }

    public String getTargetText() {
        return targetText;
    }

    public void setTargetText(String targetText) {
        this.targetText = targetText;
        this.modificationDate = LocalDateTime.now();
    }

    public String getSourceLang() {
        return sourceLang;
    }

    public void setSourceLang(String sourceLang) {
        this.sourceLang = sourceLang;
    }

    public String getTargetLang() {
        return targetLang;
    }

    public void setTargetLang(String targetLang) {
        this.targetLang = targetLang;
    }

    public String getSourceHash() {
        return sourceHash;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(LocalDateTime modificationDate) {
        this.modificationDate = modificationDate;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getSubjectField() {
        return subjectField;
    }

    public void setSubjectField(String subjectField) {
        this.subjectField = subjectField;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public int getQualityScore() {
        return qualityScore;
    }

    public void setQualityScore(int qualityScore) {
        this.qualityScore = qualityScore;
    }

    public int getUsageCount() {
        return usageCount;
    }

    public void setUsageCount(int usageCount) {
        this.usageCount = usageCount;
    }

    public void incrementUsageCount() {
        this.usageCount++;
    }

    public String getPreviousSource() {
        return previousSource;
    }

    public void setPreviousSource(String previousSource) {
        this.previousSource = previousSource;
    }

    public String getNextSource() {
        return nextSource;
    }

    public void setNextSource(String nextSource) {
        this.nextSource = nextSource;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public int getSegmentNumber() {
        return segmentNumber;
    }

    public void setSegmentNumber(int segmentNumber) {
        this.segmentNumber = segmentNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TranslationUnit that = (TranslationUnit) o;
        return Objects.equals(sourceText, that.sourceText) &&
               Objects.equals(targetText, that.targetText) &&
               Objects.equals(sourceLang, that.sourceLang) &&
               Objects.equals(targetLang, that.targetLang);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceText, targetText, sourceLang, targetLang);
    }

    @Override
    public String toString() {
        return String.format("TU[%s->%s: %s -> %s]",
                sourceLang, targetLang, sourceText, targetText);
    }
}
