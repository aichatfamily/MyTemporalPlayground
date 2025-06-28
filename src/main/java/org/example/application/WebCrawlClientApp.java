package org.example.application;

import io.temporal.client.WorkflowClient;
import io.temporal.serviceclient.WorkflowServiceStubs;
import org.example.webcrawl.WebCrawlService;
import org.example.webcrawl.model.OutputFormat;
import org.example.webcrawl.model.WebCrawlRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class WebCrawlClientApp {
    
    private static final Logger logger = LoggerFactory.getLogger(WebCrawlClientApp.class);
    
    public static void main(String[] args) {
        WorkflowServiceStubs service = WorkflowServiceStubs.newLocalServiceStubs();
        WorkflowClient client = WorkflowClient.newInstance(service);
        WebCrawlService webCrawlService = new WebCrawlService(client);

        // Example 1: yahoo.com with JSON and CSV output
        logger.info("Starting workflow for yahoo.com with JSON and CSV output...");
        List<String> result1 = webCrawlService.crawlWebsite("yahoo.com", 
                Arrays.asList(OutputFormat.JSON, OutputFormat.CSV));
        logger.info("Generated files for yahoo.com: {}", result1);

        // Example 2: google.com with all formats
        logger.info("Starting workflow for google.com with all output formats...");
        List<String> result2 = webCrawlService.crawlWebsite("google.com",
                Arrays.asList(OutputFormat.JSON, OutputFormat.CSV, OutputFormat.XML));
        logger.info("Generated files for google.com: {}", result2);

        // Example 3: github.com with XML only using request object
        WebCrawlRequest request3 = new WebCrawlRequest("github.com", List.of(OutputFormat.XML));
        logger.info("Starting workflow for github.com with XML output only...");
        List<String> result3 = webCrawlService.crawlWebsite(request3);
        logger.info("Generated files for github.com: {}", result3);

        System.exit(0);
    }
}