package org.example.application;

import io.temporal.client.WorkflowClient;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import org.example.greeting.GreetingService;
import org.example.greeting.activity.GreetingActivityImpl;
import org.example.greeting.workflow.GreetingWorkflowImpl;

public class WorkerApp {
    
    public static void main(String[] args) {
        WorkflowServiceStubs service = WorkflowServiceStubs.newLocalServiceStubs();
        WorkflowClient client = WorkflowClient.newInstance(service);
        WorkerFactory factory = WorkerFactory.newInstance(client);
        
        Worker worker = factory.newWorker(GreetingService.TASK_QUEUE);
        worker.registerWorkflowImplementationTypes(GreetingWorkflowImpl.class);
        worker.registerActivitiesImplementations(new GreetingActivityImpl());
        
        factory.start();
        
        System.out.println("Worker started for task queue: " + GreetingService.TASK_QUEUE);
    }
}