package application.scheduler;

import application.crawler.Crawler;
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
    private final static String CRAWL_JOB_THREAD_NAME = "CrawlerScheduler-%d";
    private final static String CRAWL_JOB_FAILURE_FORMAT = "Unable to complete crawl job for url: %s";

    private final Crawler crawler;

    //Normally we would have a constantly running threadpool, and would re-use the threads.
    // I would select a number of threads based on logical processors of current machine.
    //This implementation is technically more heavy since its expensive to spawn a new thread for each request.
    public void schedule(Job job, int numThreads) {
        log.info(String.format("Creating thread pool with %s threads", numThreads));
        ExecutorService executorService = newFixedThreadPool(numThreads, createThreadFactory());

        job.getCrawlJobs()
                .stream()
                .map(this::createRunnableCrawl)
                .forEach(executorService::submit);

        //Orderly shutdown, no new jobs accepted
        executorService.shutdown();
    }

    private Runnable createRunnableCrawl(CrawlJob crawlJob) {
        return () -> {
            try {
                crawler.getImages(crawlJob);
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
