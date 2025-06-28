package org.example.greeting.workflow;

import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.Workflow;
import org.example.greeting.activity.GreetingActivity;

import java.time.Duration;

public class GreetingWorkflowImpl implements GreetingWorkflow {

    private final GreetingActivity activity = Workflow.newActivityStub(
        GreetingActivity.class,
        ActivityOptions.newBuilder()
            .setStartToCloseTimeout(Duration.ofSeconds(30))
            .build()
    );

    @Override
    public String sayHello(String name) {
        return activity.createGreeting(name);
    }
}