package org.example.webcrawl.workflow;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface CsvOutputWorkflow {
    
    @WorkflowMethod
    String generateCsvOutput(String url, String robotsContent, String homepageContent);
}