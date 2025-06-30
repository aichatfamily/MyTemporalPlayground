package org.example.webcrawl.workflow;

import io.temporal.testing.TestWorkflowExtension;
import org.example.webcrawl.activity.OutputActivityImpl;
import org.example.webcrawl.activity.WebCrawlActivityImpl;
import org.example.webcrawl.model.OutputFormat;
import org.example.webcrawl.model.WebCrawlRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


class WebCrawlWorkflowIntegrationTest {

    @RegisterExtension
    public final TestWorkflowExtension testWorkflowExtension = TestWorkflowExtension.newBuilder()
            .registerWorkflowImplementationTypes(
                    WebCrawlWorkflowImpl.class,
                    JsonOutputWorkflowImpl.class,
                    CsvOutputWorkflowImpl.class,
                    XmlOutputWorkflowImpl.class
            )
            .setActivityImplementations(new WebCrawlActivityImpl(), new OutputActivityImpl())
            .build();


    @Test  
    void testCrawlWebsite_JsonOnly_CompletesSuccessfully(WebCrawlWorkflow workflow) {
        WebCrawlRequest request = new WebCrawlRequest("example.com", List.of(OutputFormat.JSON));
        
        // Execute workflow
        List<String> result = workflow.crawlWebsite(request);
        
        // Verify result
        assertThat(result).hasSize(1);
        assertThat(result.getFirst()).contains("json");
        assertThat(result.getFirst()).contains("example_com");
    }

    @Test
    void testCrawlWebsite_AllFormats_CompletesSuccessfully(WebCrawlWorkflow workflow) {
        WebCrawlRequest request = new WebCrawlRequest("multi.com", List.of(OutputFormat.JSON, OutputFormat.CSV, OutputFormat.XML));
        
        // Execute workflow
        List<String> result = workflow.crawlWebsite(request);
        
        // Verify result
        assertThat(result).hasSize(3);
        assertThat(result).anyMatch(filename -> filename.contains("json") && filename.contains("multi_com"));
        assertThat(result).anyMatch(filename -> filename.contains("csv") && filename.contains("multi_com"));
        assertThat(result).anyMatch(filename -> filename.contains("xml") && filename.contains("multi_com"));
    }
    
    @Test
    void testCrawlWebsite_EmptyFormatsList_ReturnsEmptyList(WebCrawlWorkflow workflow) {
        String url = "empty.com";
        WebCrawlRequest request = new WebCrawlRequest(url, List.of());
        
        // Execute workflow
        List<String> result = workflow.crawlWebsite(request);
        
        // Verify result
        assertThat(result).isEmpty();
    }
}