package org.example.webcrawl.workflow;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface XmlOutputWorkflow {
    
    @WorkflowMethod
    String generateXmlOutput(String url, String robotsContent, String homepageContent);
}