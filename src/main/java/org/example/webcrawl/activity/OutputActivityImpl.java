package org.example.webcrawl.activity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OutputActivityImpl implements OutputActivity {
    
    private static final Logger logger = LoggerFactory.getLogger(OutputActivityImpl.class);

    @Override
    public String generateJsonOutput(String url, String robotsContent, String homepageContent) {
        logger.info("Generating JSON output for URL: {}", url);
        return url.replace(".", "_") + "_output.json";
    }

    @Override
    public String generateCsvOutput(String url, String robotsContent, String homepageContent) {
        logger.info("Generating CSV output for URL: {}", url);
        return url.replace(".", "_") + "_output.csv";
    }

    @Override
    public String generateXmlOutput(String url, String robotsContent, String homepageContent) {
        logger.info("Generating XML output for URL: {}", url);
        return url.replace(".", "_") + "_output.xml";
    }
}