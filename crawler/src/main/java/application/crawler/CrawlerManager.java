package application.crawler;

import application.job.CrawlJob;
import application.job.Status;
import com.google.common.base.Stopwatch;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class CrawlerManager {
    private static final int MAX_DEPTH = 2;
    private final static String CRAWL_JOB_THREAD_NAME = "CrawlerManager-%d";
    private final DocumentConnector documentConnector;
    private ConcurrentHashMap<String, Boolean> results;
    private ConcurrentHashMap<String, Boolean> visited;

    public void startCrawlJob(CrawlJob crawlJob, int numThreads) {
        log.info(String.format("Creating thread pool for crawl with %s threads", numThreads));
        log.info(String.format("Starting crawl for url: %s", crawlJob.getUrl()));
        crawlJob.setStatus(Status.IN_PROGRESS);

        ThreadPoolExecutor crawlPool = new ThreadPoolExecutor(numThreads, numThreads,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(), createThreadFactory());

        //initialize shared cache
        visited = new ConcurrentHashMap<>();
        //initialize shared results
        results = new ConcurrentHashMap<>();

        Stopwatch sw = Stopwatch.createStarted();

        new Crawler(documentConnector).crawl(crawlJob.getUrl(), MAX_DEPTH, visited, results, crawlPool);
        Duration elapsed = sw.stop().elapsed();
        log.info(String.format("Time for crawl: %s", elapsed));
        crawlJob.setCompletedValues(elapsed, results.keySet());
    }


    // Daemon so it gets automatically cleaned up by GC
    private ThreadFactory createThreadFactory() {
        return new ThreadFactoryBuilder()
                .setNameFormat(CRAWL_JOB_THREAD_NAME)
                .setDaemon(true)
                .build();
    }
}
