package org.example.greeting.activity;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface GreetingActivity {
    
    @ActivityMethod
    String createGreeting(String name);
}