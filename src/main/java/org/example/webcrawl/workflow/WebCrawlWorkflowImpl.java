package org.example.webcrawl.workflow;

import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.Workflow;
import org.example.webcrawl.activity.WebCrawlActivity;
import org.example.webcrawl.model.OutputFormat;
import org.example.webcrawl.model.WebCrawlRequest;
import org.slf4j.Logger;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class WebCrawlWorkflowImpl implements WebCrawlWorkflow {
    private static final Logger logger = Workflow.getLogger(WebCrawlWorkflowImpl.class);

    private final WebCrawlActivity activity = Workflow.newActivityStub(
            WebCrawlActivity.class,
            ActivityOptions.newBuilder()
                    .setStartToCloseTimeout(Duration.ofMinutes(5))
                    .build()
    );

    @Override
    public List<String> crawlWebsite(WebCrawlRequest request) {
        logger.info("Starting web crawl workflow for URL: {} with formats: {}", 
                   request.url(), request.outputFormats());

        String url = request.url();
        List<String> generatedFiles = new ArrayList<>();

        // Step 1: Search for robots.txt
        String robotsUrl = activity.searchRobotsTxt(url);
        
        // Step 2: Download robots file
        String robotsContent = activity.downloadRobots(robotsUrl);
        
        // Step 3: Crawl homepage
        String homepageContent = activity.crawlHomepage(url);
        
        // Step 4: Generate output files based on requested formats
        for (OutputFormat format : request.outputFormats()) {
            String filename = generateOutputFile(format, url, robotsContent, homepageContent);
            generatedFiles.add(filename);
        }

        logger.info("Web crawl workflow completed. Generated files: {}", generatedFiles);
        return generatedFiles;
    }

    private String generateOutputFile(OutputFormat format, String url, String robotsContent, String homepageContent) {
        logger.info("Generating output file in format: {}", format);

        return switch (format) {
            case JSON -> activity.outputToJson(url, robotsContent, homepageContent);
            case CSV -> activity.outputToCsv(url, robotsContent, homepageContent);
            case XML -> activity.outputToXml(url, robotsContent, homepageContent);
        };
    }
}