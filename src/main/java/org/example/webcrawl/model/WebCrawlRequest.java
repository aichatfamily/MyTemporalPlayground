package org.example.webcrawl.model;

import java.util.List;

public record WebCrawlRequest(String url, List<OutputFormat> outputFormats) {
}