package application.crawler;

import application.job.CrawlJob;
import application.job.Status;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
@Slf4j
@Component
public class Crawler {
    private static final int MAX_DEPTH = 2;
    private static final String USER_AGENT = "Mozilla/5.0 (Macintosh; U; Intel Mac OS X; de-de) AppleWebKit/523.10.3 (KHTML, like Gecko) Version/3.0.4 Safari/523.10";

    public void getImgs(CrawlJob crawlJob){
        try{
            Stopwatch sw = Stopwatch.createStarted();
            List<String> val = crawl(crawlJob.getUrl(), 0, Lists.newArrayList());
            log.debug(String.format("Time for crawl: %s", sw.stop().elapsed()));
            crawlJob.setResults(val);
        }catch (Exception e) {
            crawlJob.setStatus(Status.ERROR);
        }
    }

    private List<String> crawl(String URL, int depth, List<String> results) {

        if((depth < MAX_DEPTH)){
            return results;
        }
        System.out.println(">> Depth: " + depth + " [" + URL + "]");
        try {

            Document document = Jsoup
                    .connect(URL)
                    .userAgent(USER_AGENT)
                    .referrer("http://www.google.com")
                    .timeout(10 * 1000)
                    .get();

            document.select("img").forEach(ie -> {
                log.debug(String.format("adding this img: %s", ie.attr("abs:src")));
                results.add(ie.attr("abs:src"));
            });

            Elements linksOnPage = document.select("a[href]");

            depth++;

            for(Element ie : linksOnPage) {
                log.debug(String.format("checking this path: %s", ie.attr("abs:href")));
                crawl(ie.attr("abs:href"), depth, results);
            }
            return results;
        } catch (IOException e) {
            log.error(String.format("For %s : %s", URL, e.getMessage()));
            throw new RuntimeException("invalid message", e);
        }
    }
}

