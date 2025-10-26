package com.translationpro.core.document;

import com.google.inject.Singleton;
import com.ibm.icu.text.BreakIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Implementation of document service with basic text file support.
 */
@Singleton
public class DocumentServiceImpl implements DocumentService {

    private static final Logger logger = LoggerFactory.getLogger(DocumentServiceImpl.class);

    private static final List<String> SUPPORTED_FORMATS = Arrays.asList(
            ".txt", ".html", ".xml", ".properties", ".json"
    );

    @Override
    public Document loadDocument(Path filePath, String sourceLang, String targetLang) {
        try {
            String fileName = filePath.getFileName().toString();
            String extension = getFileExtension(fileName);

            if (!isFormatSupported(extension)) {
                throw new UnsupportedOperationException("Unsupported file format: " + extension);
            }

            Document document = new Document(fileName, filePath, extension);
            document.setSourceLang(sourceLang);
            document.setTargetLang(targetLang);

            // For now, only handle text files
            if (".txt".equals(extension)) {
                loadTextDocument(document, filePath, sourceLang);
            } else {
                // Other formats would be handled by specific filters
                logger.warn("Format {} not fully implemented, treating as text", extension);
                loadTextDocument(document, filePath, sourceLang);
            }

            logger.info("Loaded document: {} with {} segments", fileName, document.getTotalSegments());
            return document;

        } catch (IOException e) {
            logger.error("Failed to load document", e);
            throw new RuntimeException("Failed to load document", e);
        }
    }

    private void loadTextDocument(Document document, Path filePath, String sourceLang) throws IOException {
        String content = Files.readString(filePath);
        List<String> segments = segmentText(content, sourceLang);

        int segmentNumber = 1;
        for (String segmentText : segments) {
            if (!segmentText.trim().isEmpty()) {
                Segment segment = new Segment(segmentNumber++, segmentText);
                document.addSegment(segment);
            }
        }
    }

    @Override
    public void saveDocument(Document document, Path outputPath) {
        try {
            StringBuilder output = new StringBuilder();

            for (Segment segment : document.getSegments()) {
                String text = segment.isTranslated() ? segment.getTargetText() : segment.getSourceText();
                output.append(text).append("\n");
            }

            Files.writeString(outputPath, output.toString());
            logger.info("Saved document: {}", outputPath);

        } catch (IOException e) {
            logger.error("Failed to save document", e);
            throw new RuntimeException("Failed to save document", e);
        }
    }

    @Override
    public List<String> getSupportedFormats() {
        return new ArrayList<>(SUPPORTED_FORMATS);
    }

    @Override
    public boolean isFormatSupported(String extension) {
        return SUPPORTED_FORMATS.contains(extension.toLowerCase());
    }

    @Override
    public List<String> segmentText(String text, String lang) {
        List<String> segments = new ArrayList<>();

        try {
            // Use ICU4J for sentence segmentation
            Locale locale = Locale.forLanguageTag(lang);
            BreakIterator sentenceIterator = BreakIterator.getSentenceInstance(locale);
            sentenceIterator.setText(text);

            int start = sentenceIterator.first();
            int end = sentenceIterator.next();

            while (end != BreakIterator.DONE) {
                String segment = text.substring(start, end).trim();
                if (!segment.isEmpty()) {
                    segments.add(segment);
                }
                start = end;
                end = sentenceIterator.next();
            }

        } catch (Exception e) {
            logger.error("Failed to segment text, falling back to simple segmentation", e);
            // Fallback: simple paragraph-based segmentation
            String[] lines = text.split("\n");
            for (String line : lines) {
                if (!line.trim().isEmpty()) {
                    segments.add(line.trim());
                }
            }
        }

        return segments;
    }

    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex).toLowerCase();
        }
        return "";
    }
}
