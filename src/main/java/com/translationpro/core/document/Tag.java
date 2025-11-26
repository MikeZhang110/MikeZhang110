package com.translationpro.core.document;

/**
 * Represents an inline tag (formatting, placeholder, etc.) within a segment.
 */
public class Tag {

    private String type;
    private String name;
    private String value;
    private int position;
    private boolean paired; // true for <tag>...</tag>, false for <tag/>

    public Tag(String type, String name, int position) {
        this.type = type;
        this.name = name;
        this.position = position;
        this.paired = false;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isPaired() {
        return paired;
    }

    public void setPaired(boolean paired) {
        this.paired = paired;
    }

    @Override
    public String toString() {
        return String.format("Tag[%s:%s at %d]", type, name, position);
    }
}
