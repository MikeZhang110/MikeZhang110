package com.translationpro.core.tm;

import java.util.List;

/**
 * Service interface for translation memory operations.
 *
 * This service provides methods for storing, retrieving, and searching translation units.
 */
public interface TranslationMemoryService {

    /**
     * Initialize the translation memory database.
     */
    void initialize();

    /**
     * Add a translation unit to the memory.
     *
     * @param translationUnit the translation unit to add
     * @return the saved translation unit with generated ID
     */
    TranslationUnit addTranslationUnit(TranslationUnit translationUnit);

    /**
     * Update an existing translation unit.
     *
     * @param translationUnit the translation unit to update
     */
    void updateTranslationUnit(TranslationUnit translationUnit);

    /**
     * Delete a translation unit.
     *
     * @param id the ID of the translation unit to delete
     */
    void deleteTranslationUnit(Long id);

    /**
     * Search for fuzzy matches for a given source text.
     *
     * @param sourceText the source text to search for
     * @param sourceLang the source language
     * @param targetLang the target language
     * @param threshold minimum match score (0-100)
     * @param maxResults maximum number of results to return
     * @return list of fuzzy matches, sorted by match score
     */
    List<FuzzyMatch> searchFuzzyMatches(
            String sourceText,
            String sourceLang,
            String targetLang,
            int threshold,
            int maxResults
    );

    /**
     * Search for fuzzy matches with context awareness.
     *
     * @param sourceText the source text to search for
     * @param previousSource the previous segment (for context)
     * @param nextSource the next segment (for context)
     * @param sourceLang the source language
     * @param targetLang the target language
     * @param threshold minimum match score (0-100)
     * @param maxResults maximum number of results to return
     * @return list of fuzzy matches, sorted by match score
     */
    List<FuzzyMatch> searchFuzzyMatchesWithContext(
            String sourceText,
            String previousSource,
            String nextSource,
            String sourceLang,
            String targetLang,
            int threshold,
            int maxResults
    );

    /**
     * Get an exact match for a given source text.
     *
     * @param sourceText the source text to search for
     * @param sourceLang the source language
     * @param targetLang the target language
     * @return the translation unit if found, null otherwise
     */
    TranslationUnit getExactMatch(String sourceText, String sourceLang, String targetLang);

    /**
     * Import translation units from a TMX file.
     *
     * @param tmxFilePath path to the TMX file
     * @return number of translation units imported
     */
    int importFromTMX(String tmxFilePath);

    /**
     * Export translation units to a TMX file.
     *
     * @param tmxFilePath path to the TMX file
     * @param sourceLang source language filter (null for all)
     * @param targetLang target language filter (null for all)
     * @return number of translation units exported
     */
    int exportToTMX(String tmxFilePath, String sourceLang, String targetLang);

    /**
     * Get statistics about the translation memory.
     *
     * @return translation memory statistics
     */
    TMStatistics getStatistics();

    /**
     * Clear all translation units from the memory.
     */
    void clear();

    /**
     * Close the translation memory and release resources.
     */
    void close();
}
