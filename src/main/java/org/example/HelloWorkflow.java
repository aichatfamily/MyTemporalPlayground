package org.example;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface HelloWorkflow {
    
    @WorkflowMethod
    String sayHello(String name);
}