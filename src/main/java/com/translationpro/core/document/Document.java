package com.translationpro.core.document;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a document being translated.
 */
public class Document {

    private String fileName;
    private Path filePath;
    private String fileFormat;
    private List<Segment> segments;
    private int currentSegmentIndex;
    private String sourceLang;
    private String targetLang;

    public Document(String fileName, Path filePath, String fileFormat) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileFormat = fileFormat;
        this.segments = new ArrayList<>();
        this.currentSegmentIndex = 0;
    }

    public String getFileName() {
        return fileName;
    }

    public Path getFilePath() {
        return filePath;
    }

    public String getFileFormat() {
        return fileFormat;
    }

    public List<Segment> getSegments() {
        return segments;
    }

    public void addSegment(Segment segment) {
        this.segments.add(segment);
    }

    public Segment getSegment(int index) {
        if (index >= 0 && index < segments.size()) {
            return segments.get(index);
        }
        return null;
    }

    public Segment getCurrentSegment() {
        return getSegment(currentSegmentIndex);
    }

    public int getCurrentSegmentIndex() {
        return currentSegmentIndex;
    }

    public void setCurrentSegmentIndex(int index) {
        if (index >= 0 && index < segments.size()) {
            this.currentSegmentIndex = index;
        }
    }

    public boolean hasNextSegment() {
        return currentSegmentIndex < segments.size() - 1;
    }

    public boolean hasPreviousSegment() {
        return currentSegmentIndex > 0;
    }

    public Segment nextSegment() {
        if (hasNextSegment()) {
            currentSegmentIndex++;
            return getCurrentSegment();
        }
        return null;
    }

    public Segment previousSegment() {
        if (hasPreviousSegment()) {
            currentSegmentIndex--;
            return getCurrentSegment();
        }
        return null;
    }

    public int getTotalSegments() {
        return segments.size();
    }

    public int getTranslatedSegments() {
        return (int) segments.stream().filter(Segment::isTranslated).count();
    }

    public int getConfirmedSegments() {
        return (int) segments.stream().filter(Segment::isConfirmed).count();
    }

    public double getCompletionPercentage() {
        if (segments.isEmpty()) {
            return 0;
        }
        return (double) getTranslatedSegments() / segments.size() * 100;
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

    @Override
    public String toString() {
        return String.format("Document[%s: %d segments, %.1f%% complete]",
                fileName, getTotalSegments(), getCompletionPercentage());
    }
}
