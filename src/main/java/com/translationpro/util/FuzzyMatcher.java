package com.translationpro.util;

/**
 * Utility class for fuzzy string matching.
 *
 * Uses Levenshtein distance algorithm for calculating similarity between strings.
 */
public class FuzzyMatcher {

    /**
     * Calculate similarity between two strings as a percentage (0-100).
     *
     * @param s1 first string
     * @param s2 second string
     * @return similarity score (0-100)
     */
    public int calculateSimilarity(String s1, String s2) {
        if (s1 == null || s2 == null) {
            return 0;
        }

        if (s1.equals(s2)) {
            return 100;
        }

        int distance = levenshteinDistance(s1, s2);
        int maxLength = Math.max(s1.length(), s2.length());

        if (maxLength == 0) {
            return 100;
        }

        double similarity = 1.0 - ((double) distance / maxLength);
        return (int) Math.round(similarity * 100);
    }

    /**
     * Calculate Levenshtein distance between two strings.
     *
     * @param s1 first string
     * @param s2 second string
     * @return edit distance
     */
    public int levenshteinDistance(String s1, String s2) {
        int len1 = s1.length();
        int len2 = s2.length();

        // Optimize for empty strings
        if (len1 == 0) return len2;
        if (len2 == 0) return len1;

        // Create distance matrix
        int[][] dp = new int[len1 + 1][len2 + 1];

        // Initialize first row and column
        for (int i = 0; i <= len1; i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= len2; j++) {
            dp[0][j] = j;
        }

        // Fill matrix using dynamic programming
        for (int i = 1; i <= len1; i++) {
            for (int j = 1; j <= len2; j++) {
                int cost = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : 1;

                dp[i][j] = Math.min(
                    Math.min(
                        dp[i - 1][j] + 1,      // deletion
                        dp[i][j - 1] + 1       // insertion
                    ),
                    dp[i - 1][j - 1] + cost    // substitution
                );
            }
        }

        return dp[len1][len2];
    }

    /**
     * Calculate word-level similarity (useful for longer texts).
     *
     * @param s1 first string
     * @param s2 second string
     * @return similarity score (0-100)
     */
    public int calculateWordSimilarity(String s1, String s2) {
        if (s1 == null || s2 == null) {
            return 0;
        }

        String[] words1 = s1.trim().split("\\s+");
        String[] words2 = s2.trim().split("\\s+");

        if (words1.length == 0 || words2.length == 0) {
            return 0;
        }

        // Count matching words
        int matchingWords = 0;
        for (String word1 : words1) {
            for (String word2 : words2) {
                if (word1.equalsIgnoreCase(word2)) {
                    matchingWords++;
                    break;
                }
            }
        }

        double similarity = (double) matchingWords / Math.max(words1.length, words2.length);
        return (int) Math.round(similarity * 100);
    }

    /**
     * Calculate combined character and word similarity.
     *
     * @param s1 first string
     * @param s2 second string
     * @return combined similarity score (0-100)
     */
    public int calculateCombinedSimilarity(String s1, String s2) {
        int charSim = calculateSimilarity(s1, s2);
        int wordSim = calculateWordSimilarity(s1, s2);

        // Weight character similarity more for short strings, word similarity for longer ones
        int wordCount = Math.max(s1.split("\\s+").length, s2.split("\\s+").length);
        if (wordCount < 5) {
            return (int) Math.round(charSim * 0.7 + wordSim * 0.3);
        } else {
            return (int) Math.round(charSim * 0.5 + wordSim * 0.5);
        }
    }
}
