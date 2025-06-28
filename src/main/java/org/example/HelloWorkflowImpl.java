package org.example;

import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.Workflow;

import java.time.Duration;

public class HelloWorkflowImpl implements HelloWorkflow {

    private final HelloActivity activity = Workflow.newActivityStub(
        HelloActivity.class,
        ActivityOptions.newBuilder()
            .setStartToCloseTimeout(Duration.ofSeconds(30))
            .build()
    );

    @Override
    public String sayHello(String name) {
        return activity.createGreeting(name);
    }
}