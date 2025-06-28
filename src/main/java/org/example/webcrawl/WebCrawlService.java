package org.example.webcrawl;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import org.example.webcrawl.model.OutputFormat;
import org.example.webcrawl.model.WebCrawlRequest;
import org.example.webcrawl.workflow.WebCrawlWorkflow;

import java.util.List;

public class WebCrawlService {
    
    public static final String TASK_QUEUE = "webcrawl-queue";
    
    private final WorkflowClient client;
    
    public WebCrawlService(WorkflowClient client) {
        this.client = client;
    }
    
    public List<String> crawlWebsite(String url, List<OutputFormat> outputFormats) {
        WebCrawlRequest request = new WebCrawlRequest(url, outputFormats);
        
        WorkflowOptions options = WorkflowOptions.newBuilder()
            .setTaskQueue(TASK_QUEUE)
            .setWorkflowId("webcrawl-" + url.replace(".", "-") + "-" + System.currentTimeMillis())
            .build();
            
        WebCrawlWorkflow workflow = client.newWorkflowStub(WebCrawlWorkflow.class, options);
        return workflow.crawlWebsite(request);
    }
    
    public List<String> crawlWebsite(WebCrawlRequest request) {
        WorkflowOptions options = WorkflowOptions.newBuilder()
            .setTaskQueue(TASK_QUEUE)
            .setWorkflowId("webcrawl-" + request.url().replace(".", "-") + "-" + System.currentTimeMillis())
            .build();
            
        WebCrawlWorkflow workflow = client.newWorkflowStub(WebCrawlWorkflow.class, options);
        return workflow.crawlWebsite(request);
    }
}