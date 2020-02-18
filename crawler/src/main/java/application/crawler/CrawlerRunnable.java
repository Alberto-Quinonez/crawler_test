package application.crawler;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.UnsupportedMimeTypeException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

@Slf4j
@AllArgsConstructor
public class CrawlerRunnable implements Runnable{
    private static final int MAX_DEPTH = 2;
    private final String url;
    private final ExecutorService executor;
    private final DocumentConnector documentConnector;
    private final int depth;
    private ConcurrentHashMap<String, Boolean> results;
    private ConcurrentHashMap<String, Boolean> visited;

    @Override
    public void run() {
        if (checkContinue(url, depth, visited)) {
            return;
        }
        log.info(String.format(">> Depth: %s [%s]", depth, url));

        try {
            visited.put(url, true);
            Document document = documentConnector.getDocument(url);

            document.select("img").forEach(ie -> {
                log.info(String.format("adding this img: %s", ie.attr("abs:src")));
                results.put(ie.attr("abs:src"), true);
            });

            Elements linksOnPage = document.select("a[href]");

            for (Element ie : linksOnPage) {
                log.info(String.format("checking this path: %s", ie.attr("abs:href")));
                executor.submit(new CrawlerRunnable(ie.attr("abs:href"), executor, documentConnector, depth + 1, results, visited));
            }
        } catch (UnsupportedMimeTypeException e) {
            log.info(String.format("Skipping URL [%s]. Invalid MIME type [%s] for url, e.", url, e.getMimeType()), e);
        } catch (IOException e) {
            log.error(String.format("Network error for [%s].", url), e);
        }
    }

    private boolean checkContinue(String URL, int depth, ConcurrentHashMap<String, Boolean> visited) {
        return visited.containsKey(URL) || URL.isEmpty() || depth >= MAX_DEPTH;
    }
}

