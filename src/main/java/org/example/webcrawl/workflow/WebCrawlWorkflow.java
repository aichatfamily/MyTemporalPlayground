package org.example.webcrawl.workflow;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;
import org.example.webcrawl.model.WebCrawlRequest;
import java.util.List;

@WorkflowInterface
public interface WebCrawlWorkflow {
    
    @WorkflowMethod
    List<String> crawlWebsite(WebCrawlRequest request);
}