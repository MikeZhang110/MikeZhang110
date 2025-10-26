package com.translationpro.core.tm;

import java.util.HashMap;
import java.util.Map;

/**
 * Statistics for a translation memory.
 */
public class TMStatistics {

    private long totalTranslationUnits;
    private Map<String, Long> languagePairCounts;
    private Map<String, Long> projectCounts;
    private long totalWords;
    private long averageWordsPerUnit;

    public TMStatistics() {
        this.languagePairCounts = new HashMap<>();
        this.projectCounts = new HashMap<>();
    }

    public long getTotalTranslationUnits() {
        return totalTranslationUnits;
    }

    public void setTotalTranslationUnits(long totalTranslationUnits) {
        this.totalTranslationUnits = totalTranslationUnits;
    }

    public Map<String, Long> getLanguagePairCounts() {
        return languagePairCounts;
    }

    public void setLanguagePairCounts(Map<String, Long> languagePairCounts) {
        this.languagePairCounts = languagePairCounts;
    }

    public Map<String, Long> getProjectCounts() {
        return projectCounts;
    }

    public void setProjectCounts(Map<String, Long> projectCounts) {
        this.projectCounts = projectCounts;
    }

    public long getTotalWords() {
        return totalWords;
    }

    public void setTotalWords(long totalWords) {
        this.totalWords = totalWords;
    }

    public long getAverageWordsPerUnit() {
        return averageWordsPerUnit;
    }

    public void setAverageWordsPerUnit(long averageWordsPerUnit) {
        this.averageWordsPerUnit = averageWordsPerUnit;
    }

    @Override
    public String toString() {
        return String.format("TMStatistics[units=%d, words=%d, avgWords=%d, langPairs=%d, projects=%d]",
                totalTranslationUnits, totalWords, averageWordsPerUnit,
                languagePairCounts.size(), projectCounts.size());
    }
}
