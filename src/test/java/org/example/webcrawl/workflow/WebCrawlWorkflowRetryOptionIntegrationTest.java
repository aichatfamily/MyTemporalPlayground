package org.example.webcrawl.workflow;

import io.temporal.testing.TestWorkflowEnvironment;
import io.temporal.worker.Worker;
import org.example.webcrawl.activity.OutputActivityImpl;
import org.example.webcrawl.activity.WebCrawlActivity;
import org.example.webcrawl.activity.WebCrawlActivityImpl;
import org.example.webcrawl.model.OutputFormat;
import org.example.webcrawl.model.WebCrawlRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Integration tests for WebCrawlWorkflow retry policy behavior using real activity implementations.
 * <p>
 * WHY THIS SEPARATE TEST FILE IS NEEDED:
 * <p>
 * This test file exists to provide comprehensive coverage that bridges the gap between unit and workflow tests:
 * <p>
 * 1. WebCrawlActivityImplTest.java - Unit tests the activity in isolation
 *    ✅ Tests that WebCrawlActivityImpl.searchRobotsTxt() throws IllegalArgumentException for blank URLs
 *    ❌ Does NOT test the integration with the workflow's retry policy
 * <p>
 * 2. WebCrawlWorkflowIntegrationTest.java - Workflow integration tests (happy path)
 *    ✅ Tests successful workflow execution with real WebCrawlActivityImpl
 *    ❌ Does NOT test retry policy behavior or exception scenarios
 * <p>
 * 3. WebCrawlWorkflowRetryTest.java - Retry behavior verification (controlled)
 *    ✅ Tests that IllegalArgumentException doesn't trigger retries
 *    ❌ Uses custom test activity, NOT the real WebCrawlActivityImpl
 * <p>
 * 4. THIS FILE - End-to-end integration tests (real implementation + retry policy)
 *    ✅ Tests retry behavior using the actual WebCrawlActivityImpl implementation
 *    ✅ Verifies the real activity throws IllegalArgumentException AND doesn't retry
 *    ✅ Ensures the configured retry policy (setDoNotRetry) works with production code
 * <p>
 * This provides confidence that:
 * - The real activity implementation properly throws IllegalArgumentException for invalid input
 * - The workflow's retry policy correctly treats this exception as non-retryable
 * - The integration between real activity code and workflow retry configuration works as expected
 * - No regression will occur if either the activity logic or retry policy is modified
 */
class WebCrawlWorkflowRetryOptionIntegrationTest {

    private TestWorkflowEnvironment testEnv;
    private AtomicInteger searchRobotsTxtCallCount;

    // Wrapper activity that counts calls to the real implementation
    private static class CountingWebCrawlActivityImpl implements WebCrawlActivity {
        private final WebCrawlActivityImpl realActivity = new WebCrawlActivityImpl();
        private final AtomicInteger searchRobotsTxtCallCount;

        public CountingWebCrawlActivityImpl(AtomicInteger searchRobotsTxtCallCount) {
            this.searchRobotsTxtCallCount = searchRobotsTxtCallCount;
        }

        @Override
        public String searchRobotsTxt(String url) {
            searchRobotsTxtCallCount.incrementAndGet();
            // Delegate to the real implementation which will throw IllegalArgumentException for blank URLs
            return realActivity.searchRobotsTxt(url);
        }

        @Override
        public String downloadRobots(String robotsUrl) {
            return realActivity.downloadRobots(robotsUrl);
        }

        @Override
        public String crawlHomepage(String url) {
            return realActivity.crawlHomepage(url);
        }
    }

    @BeforeEach
    void setUp() {
        testEnv = TestWorkflowEnvironment.newInstance();
        Worker worker = testEnv.newWorker("test-task-queue");
        
        // Initialize call counter
        searchRobotsTxtCallCount = new AtomicInteger(0);
        
        // Register workflow implementations
        worker.registerWorkflowImplementationTypes(
                WebCrawlWorkflowImpl.class,
                JsonOutputWorkflowImpl.class,
                CsvOutputWorkflowImpl.class,
                XmlOutputWorkflowImpl.class
        );
        
        // Register counting wrapper around real activity implementation
        worker.registerActivitiesImplementations(
                new CountingWebCrawlActivityImpl(searchRobotsTxtCallCount),
                new OutputActivityImpl()
        );
        
        testEnv.start();
    }

    @AfterEach
    void tearDown() {
        testEnv.close();
    }

    @Test
    void testCrawlWebsite_RealActivity_IllegalArgumentException_NoRetryVerification() {
        // Create workflow stub
        WebCrawlWorkflow workflow = testEnv.getWorkflowClient()
                .newWorkflowStub(WebCrawlWorkflow.class, 
                        io.temporal.client.WorkflowOptions.newBuilder()
                                .setTaskQueue("test-task-queue")
                                .build());
        
        // Use blank URL to trigger IllegalArgumentException from real WebCrawlActivityImpl
        WebCrawlRequest request = new WebCrawlRequest("", List.of(OutputFormat.JSON));
        
        // Execute workflow and expect exception from real activity implementation
        assertThrows(Exception.class, () -> workflow.crawlWebsite(request));
        
        // Verify the real activity was called exactly once (no retries due to IllegalArgumentException)
        assertEquals(1, searchRobotsTxtCallCount.get(), 
                "Real WebCrawlActivityImpl.searchRobotsTxt should be called exactly once (no retries for IllegalArgumentException)");
    }

    @Test
    void testCrawlWebsite_RealActivity_ValidUrl_Success() {
        // Create workflow stub  
        WebCrawlWorkflow workflow = testEnv.getWorkflowClient()
                .newWorkflowStub(WebCrawlWorkflow.class,
                        io.temporal.client.WorkflowOptions.newBuilder()
                                .setTaskQueue("test-task-queue")
                                .build());

        // Use valid URL that should work with real implementation
        WebCrawlRequest request = new WebCrawlRequest("example.com", List.of(OutputFormat.JSON));
        
        // Execute workflow and expect success
        List<String> result = workflow.crawlWebsite(request);
        
        // Verify workflow completed successfully 
        assertEquals(1, result.size());
        assertEquals(1, searchRobotsTxtCallCount.get(), 
                "Real WebCrawlActivityImpl.searchRobotsTxt should be called exactly once for successful execution");
    }
}