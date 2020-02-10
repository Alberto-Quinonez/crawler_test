package application.crawler;

import application.job.CrawlJob;
import application.job.Status;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.UnsupportedMimeTypeException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Component
public class Crawler {
    private static final int MAX_DEPTH = 2;
    private static final String USER_AGENT = "Mozilla/5.0 (Macintosh; U; Intel Mac OS X; de-de) AppleWebKit/523.10.3 (KHTML, like Gecko) Version/3.0.4 Safari/523.10";


    public void getImages(CrawlJob crawlJob) {
        try{
            log.info(String.format("Starting crawl for url: %s", crawlJob.getUrl()));
            crawlJob.setStatus(Status.IN_PROGRESS);

            //initialize cache
            HashSet<String> visited = Sets.newHashSet();

            Stopwatch sw = Stopwatch.createStarted();
            Set<String> results = crawl(crawlJob.getUrl(), 0, crawlJob.getResults(), visited);
            Duration elapsed = sw.stop().elapsed();
            log.info(String.format("Time for crawl: %s", elapsed));

            crawlJob.setTimeElapsed(elapsed.toString());
            crawlJob.setStatus(Status.COMPLETE);
            crawlJob.setResultSize(results.size());
            crawlJob.setResults(results);

        } catch (Exception e) {
            log.error(String.format("Unable to complete crawl job for url: %s : %s", crawlJob.getUrl(), e.getMessage()));
            crawlJob.setStatus(Status.ERROR);
        }
    }

    private Set<String> crawl(String URL, int depth, Set<String> results, HashSet<String> visited) {

        if(checkContinue(URL, depth, visited)){
            return results;
        }

        log.info(String.format(">> Depth: %s [%s]", depth, URL));

        try {
            visited.add(URL);

            Document document = Jsoup
                    .connect(URL)
                    .userAgent(USER_AGENT)
                    .referrer("http://www.google.com")
                    .timeout(10 * 1000)
                    .get();

            document.select("img").forEach(ie -> {
                log.info(String.format("adding this img: %s", ie.attr("abs:src")));
                results.add(ie.attr("abs:src"));
            });

            Elements linksOnPage = document.select("a[href]");

            depth++;

            for (Element ie : linksOnPage) {
                log.info(String.format("checking this path: %s", ie.attr("abs:href")));
                crawl(ie.attr("abs:href"), depth, results, visited);
            }
            return results;
        } catch (UnsupportedMimeTypeException e) {
            log.info(String.format("Invalid MIME type %s for url: %s, e: %s", e.getMimeType(), URL, e.getMessage()));
            log.info(String.format("Skipping URL: %s", URL));
            return results;
        } catch (IOException e) {
            log.error(String.format("Network error for %s : %s", URL, e.getMessage()));
            return results;
            //throw new RuntimeException("invalid message", e);
        }
    }

    private boolean checkContinue(String URL, int depth, HashSet<String> visited) {
        return visited.contains(URL) || depth >= MAX_DEPTH || URL.isEmpty();
    }
}

