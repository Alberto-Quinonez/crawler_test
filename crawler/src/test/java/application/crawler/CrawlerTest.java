package application.crawler;

import application.job.CrawlJob;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.Set;

import static org.mockito.Mockito.when;

@DisplayName("Crawler Test")
class CrawlerTest {
    private static final String TEST_URL = "test-url";
    private Crawler crawler;
    private CrawlJob crawlJob;
    private Set<String> results;

    @Mock
    private DocumentConnector documentConnector;
    @Mock
    private Document document;
    @Mock
    private Elements elements;



    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
        crawler = new Crawler(documentConnector);
    }

    @Nested
    class Crawl {
        @Nested
        class GivenCrawlMaxDepth {
            @BeforeEach
            void setup() throws IOException {
                when(documentConnector.getDocument(TEST_URL)).thenReturn(document);
                when(document.select("img")).thenReturn(elements);

                crawlJob = new CrawlJob(TEST_URL);



                crawler.getImages(crawlJob);
            }

            @Test
            void thenResultIsIdentical() {
            }
        }

        @Nested
        class GivenInvalidUrl {
            Throwable throwable;
            @BeforeEach
            void setup() {
            }


            @Test
            void thenThrowsInvalidUrlException() {
            }
        }

        @Nested
        class GivenInvalidThreadCount {
            Throwable throwable;
            @BeforeEach
            void setup() {
            }


            @Test
            void thenThrowsInvalidUrlException() {
            }
        }
    }
}
