package org.example;

public class HelloActivityImpl implements HelloActivity {
    
    @Override
    public String createGreeting(String name) {
        return "Hello " + name + " from Temporal!";
    }
}