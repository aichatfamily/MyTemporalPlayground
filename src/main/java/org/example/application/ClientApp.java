package org.example.application;

import io.temporal.client.WorkflowClient;
import io.temporal.serviceclient.WorkflowServiceStubs;
import org.example.greeting.GreetingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientApp {
    
    private static final Logger logger = LoggerFactory.getLogger(ClientApp.class);
    
    public static void main(String[] args) {
        WorkflowServiceStubs service = WorkflowServiceStubs.newLocalServiceStubs();
        WorkflowClient client = WorkflowClient.newInstance(service);
        
        GreetingService greetingService = new GreetingService(client);
        String result = greetingService.sayHello("World");
        
        logger.info("Greeting result: {}", result);
        System.exit(0);
    }
}