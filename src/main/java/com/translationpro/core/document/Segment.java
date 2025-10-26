package com.translationpro.core.document;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a translatable segment in a document.
 */
public class Segment {

    private int segmentNumber;
    private String sourceText;
    private String targetText;
    private boolean translated;
    private boolean confirmed;
    private List<Tag> tags;
    private String notes;
    private int matchScore; // 0-100, for TM matches

    public Segment(int segmentNumber, String sourceText) {
        this.segmentNumber = segmentNumber;
        this.sourceText = sourceText;
        this.targetText = "";
        this.translated = false;
        this.confirmed = false;
        this.tags = new ArrayList<>();
        this.matchScore = 0;
    }

    public int getSegmentNumber() {
        return segmentNumber;
    }

    public void setSegmentNumber(int segmentNumber) {
        this.segmentNumber = segmentNumber;
    }

    public String getSourceText() {
        return sourceText;
    }

    public void setSourceText(String sourceText) {
        this.sourceText = sourceText;
    }

    public String getTargetText() {
        return targetText;
    }

    public void setTargetText(String targetText) {
        this.targetText = targetText;
        this.translated = targetText != null && !targetText.isEmpty();
    }

    public boolean isTranslated() {
        return translated;
    }

    public void setTranslated(boolean translated) {
        this.translated = translated;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public void addTag(Tag tag) {
        this.tags.add(tag);
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getMatchScore() {
        return matchScore;
    }

    public void setMatchScore(int matchScore) {
        this.matchScore = matchScore;
    }

    public boolean hasExactMatch() {
        return matchScore == 100;
    }

    public boolean hasFuzzyMatch() {
        return matchScore > 0 && matchScore < 100;
    }

    public int getWordCount() {
        return sourceText.trim().split("\\s+").length;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Segment segment = (Segment) o;
        return segmentNumber == segment.segmentNumber &&
               Objects.equals(sourceText, segment.sourceText);
    }

    @Override
    public int hashCode() {
        return Objects.hash(segmentNumber, sourceText);
    }

    @Override
    public String toString() {
        return String.format("Segment[%d: %s -> %s]", segmentNumber, sourceText, targetText);
    }
}
