package org.example.greeting;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import org.example.greeting.workflow.GreetingWorkflow;

public class GreetingService {
    
    public static final String TASK_QUEUE = "greeting-task-queue";
    
    private final WorkflowClient client;
    
    public GreetingService(WorkflowClient client) {
        this.client = client;
    }
    
    public String sayHello(String name) {
        WorkflowOptions options = WorkflowOptions.newBuilder()
            .setTaskQueue(TASK_QUEUE)
            .setWorkflowId("greeting-workflow-" + System.currentTimeMillis())
            .build();
            
        GreetingWorkflow workflow = client.newWorkflowStub(GreetingWorkflow.class, options);
        return workflow.sayHello(name);
    }
}