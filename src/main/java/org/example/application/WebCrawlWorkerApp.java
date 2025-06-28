package org.example.application;

import io.temporal.client.WorkflowClient;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import org.example.webcrawl.activity.WebCrawlActivityImpl;
import org.example.webcrawl.activity.OutputActivityImpl;
import org.example.webcrawl.workflow.WebCrawlWorkflowImpl;
import org.example.webcrawl.workflow.JsonOutputWorkflowImpl;
import org.example.webcrawl.workflow.CsvOutputWorkflowImpl;
import org.example.webcrawl.workflow.XmlOutputWorkflowImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebCrawlWorkerApp {
    
    private static final Logger logger = LoggerFactory.getLogger(WebCrawlWorkerApp.class);
    
    public static void main(String[] args) {
        WorkflowServiceStubs service = WorkflowServiceStubs.newLocalServiceStubs();
        WorkflowClient client = WorkflowClient.newInstance(service);
        WorkerFactory factory = WorkerFactory.newInstance(client);
        
        Worker worker = factory.newWorker("webcrawl-queue");
        worker.registerWorkflowImplementationTypes(
                WebCrawlWorkflowImpl.class,
                JsonOutputWorkflowImpl.class,
                CsvOutputWorkflowImpl.class,
                XmlOutputWorkflowImpl.class
        );
        worker.registerActivitiesImplementations(new WebCrawlActivityImpl(), new OutputActivityImpl());
        
        factory.start();

        logger.info("WebCrawl Worker started. Listening on webcrawl-queue...");
    }
}