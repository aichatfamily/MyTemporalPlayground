package org.example.webcrawl.workflow;

import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.Workflow;
import org.example.webcrawl.activity.OutputActivity;
import org.slf4j.Logger;

import java.time.Duration;

public class CsvOutputWorkflowImpl implements CsvOutputWorkflow {
    
    private static final Logger logger = Workflow.getLogger(CsvOutputWorkflowImpl.class);
    
    private final OutputActivity outputActivity = Workflow.newActivityStub(
            OutputActivity.class,
            ActivityOptions.newBuilder()
                    .setStartToCloseTimeout(Duration.ofMinutes(2))
                    .build()
    );

    @Override
    public String generateOutput(String url, String robotsContent, String homepageContent) {
        logger.info("Starting CSV output workflow for URL: {}", url);
        return outputActivity.generateCsvOutput(url, robotsContent, homepageContent);
    }
}