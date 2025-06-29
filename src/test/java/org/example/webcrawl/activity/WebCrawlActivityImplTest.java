package org.example.webcrawl.activity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class WebCrawlActivityImplTest {

    private WebCrawlActivityImpl webCrawlActivity;

    @BeforeEach
    void setUp() {
        webCrawlActivity = new WebCrawlActivityImpl();
    }

    @Test
    void testSearchRobotsTxt_ValidDomain() {
        String result = webCrawlActivity.searchRobotsTxt("example.com");
        assertEquals("https://example.com/robots.txt", result);
    }

    @ParameterizedTest
    @CsvSource({
        "www.example.com, https://www.example.com/robots.txt",
        "subdomain.example.com, https://subdomain.example.com/robots.txt",
        "api.github.com, https://api.github.com/robots.txt"
    })
    void testSearchRobotsTxt_DomainsWithSubdomain(String domain, String expected) {
        String result = webCrawlActivity.searchRobotsTxt(domain);
        assertEquals(expected, result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "\t", "\n"})
    void testSearchRobotsTxt_BlankInput_ThrowsException(String blankUrl) {
        assertThrows(IllegalArgumentException.class, () -> webCrawlActivity.searchRobotsTxt(blankUrl));
    }

    @Test
    void testSearchRobotsTxt_NullInput_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> webCrawlActivity.searchRobotsTxt(null));
    }
}