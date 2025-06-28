package org.example.greeting.activity;

public class GreetingActivityImpl implements GreetingActivity {
    
    @Override
    public String createGreeting(String name) {
        return "Hello " + name + " from Temporal!";
    }
}