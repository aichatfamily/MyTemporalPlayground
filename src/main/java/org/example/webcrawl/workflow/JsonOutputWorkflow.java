package org.example.webcrawl.workflow;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface JsonOutputWorkflow {
    
    @WorkflowMethod
    String generateJsonOutput(String url, String robotsContent, String homepageContent);
}