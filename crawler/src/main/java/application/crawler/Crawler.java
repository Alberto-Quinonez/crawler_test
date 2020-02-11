package application.crawler;

import application.job.CrawlJob;
import application.job.Status;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Sets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.UnsupportedMimeTypeException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Component
public class Crawler {
    private static final int MAX_DEPTH = 2;
    private final DocumentConnector documentConnector;

    public void getImages(CrawlJob crawlJob) {
        log.info(String.format("Starting crawl for url: %s", crawlJob.getUrl()));
        crawlJob.setStatus(Status.IN_PROGRESS);

        //initialize cache
        Set<String> visited = Sets.newHashSet();

        Stopwatch sw = Stopwatch.createStarted();
        Set<String> results = crawl(crawlJob.getUrl(), 0, Sets.newHashSet(), visited);
        Duration elapsed = sw.stop().elapsed();
        log.info(String.format("Time for crawl: %s", elapsed));

        crawlJob.setCompletedValues(elapsed, results);
    }

    private Set<String> crawl(String URL, int depth, Set<String> results, Set<String> visited) {

        if (checkContinue(URL, depth, visited)) {
            return results;
        }

        log.info(String.format(">> Depth: %s [%s]", depth, URL));

        try {
            visited.add(URL);
            Document document = documentConnector.getDocument(URL);

            document.select("img").forEach(ie -> {
                log.info(String.format("adding this img: %s", ie.attr("abs:src")));
                results.add(ie.attr("abs:src"));
            });

            Elements linksOnPage = document.select("a[href]");

            for (Element ie : linksOnPage) {
                log.info(String.format("checking this path: %s", ie.attr("abs:href")));
                crawl(ie.attr("abs:href"), depth + 1, results, visited);
            }
            return results;
        } catch (UnsupportedMimeTypeException e) {
            log.info(String.format("Skipping URL [%s]. Invalid MIME type [%s] for url, e.", URL, e.getMimeType()), e);
            return results;
        } catch (IOException e) {
            log.error(String.format("Network error for [%s].", URL), e);
            return results;
        }
    }

    private boolean checkContinue(String URL, int depth, Set<String> visited) {
        return visited.contains(URL) || depth >= MAX_DEPTH || URL.isEmpty();
    }
}

