package org.example.webcrawl.activity;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface WebCrawlActivity {
    
    @ActivityMethod
    String searchRobotsTxt(String url);
    
    @ActivityMethod
    String downloadRobots(String robotsUrl);
    
    @ActivityMethod
    String crawlHomepage(String url);
    
    @ActivityMethod
    String outputToJson(String url, String robotsContent, String homepageContent);
    
    @ActivityMethod
    String outputToCsv(String url, String robotsContent, String homepageContent);
    
    @ActivityMethod
    String outputToXml(String url, String robotsContent, String homepageContent);
}