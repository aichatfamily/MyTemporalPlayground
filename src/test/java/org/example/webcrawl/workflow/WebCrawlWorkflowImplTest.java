package org.example.webcrawl.workflow;

import io.temporal.testing.TestWorkflowExtension;
import org.example.webcrawl.activity.OutputActivityImpl;
import org.example.webcrawl.activity.WebCrawlActivity;
import org.example.webcrawl.activity.WebCrawlActivityImpl;
import org.example.webcrawl.model.OutputFormat;
import org.example.webcrawl.model.WebCrawlRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class WebCrawlWorkflowImplTest {

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

    @Test  
    void testWorkflow_BlankURL_FailsFastWithRetryPolicy(WebCrawlWorkflow workflow) {
        // This test verifies that retry policy excludes IllegalArgumentException
        // Should fail fast (< 5 seconds) without retries
        
        WebCrawlRequest request = new WebCrawlRequest("   ", List.of(OutputFormat.JSON));
        long startTime = System.currentTimeMillis();
        
        // Execute workflow - should fail fast due to retry policy configuration
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            workflow.crawlWebsite(request);
        });
        
        long duration = System.currentTimeMillis() - startTime;
        
        // Should complete quickly (< 5 seconds) proving no retries happened
        assertThat(duration).isLessThan(5000);
        assertThat(exception.getMessage()).contains("URL cannot be blank");
    }

    @Test
    void testWorkflow_BlankURL_VerifyExceptionThrown(WebCrawlWorkflow workflow) {
        // Simple test to verify the workflow throws an exception for blank URL
        // This complements the timing test by ensuring the basic behavior works
        
        WebCrawlRequest request = new WebCrawlRequest("   ", List.of(OutputFormat.JSON));
        
        // Should throw an exception for blank URL input - that's the main assertion
        assertThrows(Exception.class, () -> {
            workflow.crawlWebsite(request);
        });
        
        // Test passes if exception is thrown - proves validation is working
        // The timing test proves it fails fast due to retry policy
    }
}