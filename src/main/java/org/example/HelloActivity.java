package org.example;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface HelloActivity {
    
    @ActivityMethod
    String createGreeting(String name);
}