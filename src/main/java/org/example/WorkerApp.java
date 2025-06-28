package org.example;

import io.temporal.client.WorkflowClient;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;

public class WorkerApp {
    
    public static final String TASK_QUEUE = "hello-world-task-queue";
    
    public static void main(String[] args) {
        WorkflowServiceStubs service = WorkflowServiceStubs.newLocalServiceStubs();
        WorkflowClient client = WorkflowClient.newInstance(service);
        WorkerFactory factory = WorkerFactory.newInstance(client);
        
        Worker worker = factory.newWorker(TASK_QUEUE);
        worker.registerWorkflowImplementationTypes(HelloWorkflowImpl.class);
        worker.registerActivitiesImplementations(new HelloActivityImpl());
        
        factory.start();
        
        System.out.println("Worker started for task queue: " + TASK_QUEUE);
    }
}