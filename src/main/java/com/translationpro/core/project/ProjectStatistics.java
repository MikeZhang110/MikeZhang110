package com.translationpro.core.project;

/**
 * Statistics for a translation project.
 */
public class ProjectStatistics {

    private int totalSegments;
    private int translatedSegments;
    private int confirmedSegments;
    private int exactMatches;
    private int fuzzyMatches;
    private int noMatches;
    private int totalWords;
    private int translatedWords;
    private double completionPercentage;
    private long translationTime; // in seconds

    public ProjectStatistics() {
    }

    public void recalculateCompletion() {
        if (totalSegments > 0) {
            this.completionPercentage = (double) translatedSegments / totalSegments * 100;
        } else {
            this.completionPercentage = 0;
        }
    }

    // Getters and setters

    public int getTotalSegments() {
        return totalSegments;
    }

    public void setTotalSegments(int totalSegments) {
        this.totalSegments = totalSegments;
        recalculateCompletion();
    }

    public int getTranslatedSegments() {
        return translatedSegments;
    }

    public void setTranslatedSegments(int translatedSegments) {
        this.translatedSegments = translatedSegments;
        recalculateCompletion();
    }

    public void incrementTranslatedSegments() {
        this.translatedSegments++;
        recalculateCompletion();
    }

    public int getConfirmedSegments() {
        return confirmedSegments;
    }

    public void setConfirmedSegments(int confirmedSegments) {
        this.confirmedSegments = confirmedSegments;
    }

    public void incrementConfirmedSegments() {
        this.confirmedSegments++;
    }

    public int getExactMatches() {
        return exactMatches;
    }

    public void setExactMatches(int exactMatches) {
        this.exactMatches = exactMatches;
    }

    public void incrementExactMatches() {
        this.exactMatches++;
    }

    public int getFuzzyMatches() {
        return fuzzyMatches;
    }

    public void setFuzzyMatches(int fuzzyMatches) {
        this.fuzzyMatches = fuzzyMatches;
    }

    public void incrementFuzzyMatches() {
        this.fuzzyMatches++;
    }

    public int getNoMatches() {
        return noMatches;
    }

    public void setNoMatches(int noMatches) {
        this.noMatches = noMatches;
    }

    public void incrementNoMatches() {
        this.noMatches++;
    }

    public int getTotalWords() {
        return totalWords;
    }

    public void setTotalWords(int totalWords) {
        this.totalWords = totalWords;
    }

    public int getTranslatedWords() {
        return translatedWords;
    }

    public void setTranslatedWords(int translatedWords) {
        this.translatedWords = translatedWords;
    }

    public double getCompletionPercentage() {
        return completionPercentage;
    }

    public long getTranslationTime() {
        return translationTime;
    }

    public void setTranslationTime(long translationTime) {
        this.translationTime = translationTime;
    }

    @Override
    public String toString() {
        return String.format("ProjectStatistics[segments=%d/%d (%.1f%%), words=%d/%d]",
                translatedSegments, totalSegments, completionPercentage,
                translatedWords, totalWords);
    }
}
