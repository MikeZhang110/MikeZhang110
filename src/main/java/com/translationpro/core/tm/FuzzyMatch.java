package com.translationpro.core.tm;

/**
 * Represents a fuzzy match result from the translation memory.
 */
public class FuzzyMatch implements Comparable<FuzzyMatch> {

    private final TranslationUnit translationUnit;
    private final int matchScore; // 0-100
    private final MatchType matchType;

    public enum MatchType {
        EXACT(100),
        CONTEXT_MATCH(95),
        FUZZY_HIGH(90),
        FUZZY_MEDIUM(75),
        FUZZY_LOW(60),
        SUBSEGMENT(50);

        private final int baseScore;

        MatchType(int baseScore) {
            this.baseScore = baseScore;
        }

        public int getBaseScore() {
            return baseScore;
        }
    }

    public FuzzyMatch(TranslationUnit translationUnit, int matchScore, MatchType matchType) {
        this.translationUnit = translationUnit;
        this.matchScore = matchScore;
        this.matchType = matchType;
    }

    public TranslationUnit getTranslationUnit() {
        return translationUnit;
    }

    public int getMatchScore() {
        return matchScore;
    }

    public MatchType getMatchType() {
        return matchType;
    }

    public String getSourceText() {
        return translationUnit.getSourceText();
    }

    public String getTargetText() {
        return translationUnit.getTargetText();
    }

    public boolean isExactMatch() {
        return matchType == MatchType.EXACT;
    }

    public boolean isContextMatch() {
        return matchType == MatchType.CONTEXT_MATCH;
    }

    public boolean isFuzzyMatch() {
        return matchType == MatchType.FUZZY_HIGH ||
               matchType == MatchType.FUZZY_MEDIUM ||
               matchType == MatchType.FUZZY_LOW;
    }

    @Override
    public int compareTo(FuzzyMatch other) {
        // Higher scores come first
        int scoreComparison = Integer.compare(other.matchScore, this.matchScore);
        if (scoreComparison != 0) {
            return scoreComparison;
        }

        // If scores are equal, prefer more recent translations
        if (this.translationUnit.getModificationDate() != null &&
            other.translationUnit.getModificationDate() != null) {
            return other.translationUnit.getModificationDate()
                    .compareTo(this.translationUnit.getModificationDate());
        }

        return 0;
    }

    @Override
    public String toString() {
        return String.format("FuzzyMatch[%d%% - %s: %s -> %s]",
                matchScore, matchType, getSourceText(), getTargetText());
    }
}
