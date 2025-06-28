package org.example.webcrawl.activity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebCrawlActivityImpl implements WebCrawlActivity {

    private static final Logger logger = LoggerFactory.getLogger(WebCrawlActivityImpl.class);

    @Override
    public String searchRobotsTxt(String url) {
        logger.info("Searching robots.txt for URL: {}", url);
        return "https://" + url + "/robots.txt";
    }

    @Override
    public String downloadRobots(String robotsUrl) {
        logger.info("Downloading robots file from: {}", robotsUrl);
        return "User-agent: *\nDisallow: /private/\nAllow: /public/";
    }

    @Override
    public String crawlHomepage(String url) {
        logger.info("Crawling homepage for URL: {}", url);
        return "<html><head><title>" + url + "</title></head><body><h1>Welcome to " + url + "</h1></body></html>";
    }

    @Override
    public String outputToJson(String url, String robotsContent, String homepageContent) {
        logger.info("Generating JSON output for URL: {}", url);
        return url.replace(".", "_") + "_output.json";
    }

    @Override
    public String outputToCsv(String url, String robotsContent, String homepageContent) {
        logger.info("Generating CSV output for URL: {}", url);
        return url.replace(".", "_") + "_output.csv";
    }

    @Override
    public String outputToXml(String url, String robotsContent, String homepageContent) {
        logger.info("Generating XML output for URL: {}", url);
        return url.replace(".", "_") + "_output.xml";
    }
}