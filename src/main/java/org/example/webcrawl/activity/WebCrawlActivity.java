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
}