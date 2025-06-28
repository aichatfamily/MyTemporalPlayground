package org.example.application;

import io.temporal.client.WorkflowClient;
import io.temporal.serviceclient.WorkflowServiceStubs;
import org.example.greeting.GreetingService;

public class ClientApp {
    
    public static void main(String[] args) {
        WorkflowServiceStubs service = WorkflowServiceStubs.newLocalServiceStubs();
        WorkflowClient client = WorkflowClient.newInstance(service);
        
        GreetingService greetingService = new GreetingService(client);
        String result = greetingService.sayHello("World");
        
        System.out.println(result);
        System.exit(0);
    }
}