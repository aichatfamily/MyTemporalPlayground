package org.example.webcrawl.model;

public enum OutputFormat {
    JSON("json"),
    CSV("csv"),
    XML("xml");

    private final String value;

    OutputFormat(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}