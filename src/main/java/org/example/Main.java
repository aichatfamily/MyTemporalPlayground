package org.example;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;

public class Main {
    
    public static void main(String[] args) {
        WorkflowServiceStubs service = WorkflowServiceStubs.newLocalServiceStubs();
        WorkflowClient client = WorkflowClient.newInstance(service);
        
        WorkflowOptions options = WorkflowOptions.newBuilder()
            .setTaskQueue(WorkerApp.TASK_QUEUE)
            .setWorkflowId("hello-world-workflow")
            .build();
            
        HelloWorkflow workflow = client.newWorkflowStub(HelloWorkflow.class, options);
        
        String result = workflow.sayHello("World");
        System.out.println(result);
        
        System.exit(0);
    }
}