package application.scheduler;

import application.crawler.Crawler;
import application.job.CrawlJob;
import application.job.Job;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

import static java.util.concurrent.Executors.newFixedThreadPool;

@Slf4j
@RequiredArgsConstructor
@Component
public class CrawlScheduler {
    private final Crawler crawler;

    public void schedule(Job job, int numThreads) {
        log.info(String.format("Creating thread pool with %s threads", numThreads));

        ExecutorService pool = newFixedThreadPool(numThreads, threadFactory("CrawlerScheduler-%d"));
        job.getCrawlJobs().forEach(crawlJob -> crawl(crawlJob, pool));

        //Orderly shutdown, no new jobs accepted
        pool.shutdown();
    }

    private Future<CrawlJob> crawl(CrawlJob crawlJob, ExecutorService executorService) {
        CompletableFuture<CrawlJob> completableFuture = new CompletableFuture<>();
        executorService.submit(() -> {
            crawler.getImages(crawlJob);
            completableFuture.complete(crawlJob);
            return null;
        });

        return completableFuture;
    }

    private ThreadFactory threadFactory(String pattern) {
        return new ThreadFactoryBuilder()
                .setNameFormat(pattern)
                .setDaemon(true)
                .build();
    }
}
