package org.example.webcrawl.workflow;

import io.temporal.workflow.WorkflowMethod;

public interface OutputWorkflow {
    
    @WorkflowMethod
    String generateOutput(String url, String robotsContent, String homepageContent);
}