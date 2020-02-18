package application.scheduler;

import application.crawler.CrawlerManager;
import application.crawler.DocumentConnector;
import application.job.CrawlJob;
import application.job.Job;
import application.job.Status;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;

import static java.util.concurrent.Executors.newFixedThreadPool;

@Slf4j
@RequiredArgsConstructor
@Component
public class CrawlScheduler {
    private final static int MAX_CONCURRENT_JOB = 4;
    private final static String CRAWL_JOB_THREAD_NAME = "CrawlerScheduler-%d";
    private final static String CRAWL_JOB_FAILURE_FORMAT = "Unable to complete crawl job for url: %s";
    private ExecutorService executorService;

    public void init() {
        log.info(String.format("Creating thread pool with %s threads", MAX_CONCURRENT_JOB));
        executorService = newFixedThreadPool(MAX_CONCURRENT_JOB, createThreadFactory());
    }

    public void schedule(Job job, int numThreads) {
        job.getCrawlJobs()
                .stream()
                .map(crawlJob -> createRunnableCrawl(crawlJob, numThreads))
                .forEach(executorService::submit);
    }

    private Runnable createRunnableCrawl(CrawlJob crawlJob, int numThreads) {
        return () -> {
            try {
                CrawlerManager manager = new CrawlerManager(new DocumentConnector());
                manager.startCrawlJob(crawlJob, numThreads);
            } catch (Exception e) {
                log.error(String.format(CRAWL_JOB_FAILURE_FORMAT, crawlJob.getUrl()), e);
                crawlJob.setStatus(Status.ERROR);
            }
        };
    }

    // Daemon so it gets automatically cleaned up by GC
    private ThreadFactory createThreadFactory() {
        return new ThreadFactoryBuilder()
                .setNameFormat(CRAWL_JOB_THREAD_NAME)
                .setDaemon(true)
                .build();
    }
}
