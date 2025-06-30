package org.example.webcrawl.workflow;

import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.workflow.ChildWorkflowOptions;
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
                    .setRetryOptions(RetryOptions.newBuilder()
                            .setDoNotRetry(IllegalArgumentException.class.getName())
                            .setMaximumAttempts(3)
                            .build())
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
        
        // Step 4: Generate output files using child workflows
        for (OutputFormat format : request.outputFormats()) {
            String filename = generateOutputFile(format, url, robotsContent, homepageContent);
            generatedFiles.add(filename);
        }

        logger.info("Web crawl workflow completed. Generated files: {}", generatedFiles);
        return generatedFiles;
    }

    private String generateOutputFile(OutputFormat format, String url, String robotsContent, String homepageContent) {
        logger.info("Generating output file in format: {} using child workflow", format);

        ChildWorkflowOptions options = ChildWorkflowOptions.newBuilder()
                .setWorkflowId("output-" + format.toString() + "-" + url.replace(".", "-") + "-" + System.currentTimeMillis())
                .build();

        return switch (format) {
            case JSON -> {
                JsonOutputWorkflow jsonWorkflow = Workflow.newChildWorkflowStub(JsonOutputWorkflow.class, options);
                yield jsonWorkflow.generateOutput(url, robotsContent, homepageContent);
            }
            case CSV -> {
                CsvOutputWorkflow csvWorkflow = Workflow.newChildWorkflowStub(CsvOutputWorkflow.class, options);
                yield csvWorkflow.generateOutput(url, robotsContent, homepageContent);
            }
            case XML -> {
                XmlOutputWorkflow xmlWorkflow = Workflow.newChildWorkflowStub(XmlOutputWorkflow.class, options);
                yield xmlWorkflow.generateOutput(url, robotsContent, homepageContent);
            }
        };
    }
}