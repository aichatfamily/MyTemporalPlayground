package org.example.webcrawl.activity;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface OutputActivity {
    
    @ActivityMethod
    String generateJsonOutput(String url, String robotsContent, String homepageContent);
    
    @ActivityMethod
    String generateCsvOutput(String url, String robotsContent, String homepageContent);
    
    @ActivityMethod
    String generateXmlOutput(String url, String robotsContent, String homepageContent);
}