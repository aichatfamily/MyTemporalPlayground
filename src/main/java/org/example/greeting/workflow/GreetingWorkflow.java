package org.example.greeting.workflow;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface GreetingWorkflow {
    
    @WorkflowMethod
    String sayHello(String name);
}