package com.translationpro.core.document;

import java.nio.file.Path;
import java.util.List;

/**
 * Service interface for document processing operations.
 */
public interface DocumentService {

    /**
     * Load a document for translation.
     *
     * @param filePath path to the document
     * @param sourceLang source language
     * @param targetLang target language
     * @return the loaded document
     */
    Document loadDocument(Path filePath, String sourceLang, String targetLang);

    /**
     * Save a translated document.
     *
     * @param document the document to save
     * @param outputPath output path
     */
    void saveDocument(Document document, Path outputPath);

    /**
     * Get supported file formats.
     *
     * @return list of supported file extensions
     */
    List<String> getSupportedFormats();

    /**
     * Check if a file format is supported.
     *
     * @param extension file extension (e.g., ".docx")
     * @return true if supported
     */
    boolean isFormatSupported(String extension);

    /**
     * Segment text according to segmentation rules.
     *
     * @param text text to segment
     * @param lang language code
     * @return list of segments
     */
    List<String> segmentText(String text, String lang);
}
