package application.scheduler;

import application.cache.CacheSupplier;
import application.crawler.Crawler;
import application.job.CrawlJob;
import application.job.Job;
import application.job.Status;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.*;

import static java.util.concurrent.Executors.newFixedThreadPool;

@RequiredArgsConstructor
@Component
public class CrawlScheduler {
    private final Crawler crawler;
    private final CacheSupplier cacheSupplier;

    public void schedule(Job job, int numThreads) {
        ExecutorService pool = newFixedThreadPool(numThreads, threadFactory("CrawlerScheduler-%d"));
        List<Future<CrawlJob>> futures = Lists.newArrayList();

        job.getCrawlJobs().forEach(crawlJob -> futures.add(crawl(crawlJob, pool)));

        futures.forEach(f -> {
            try {
                CrawlJob result = f.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });

        pool.shutdown();
    }

    private Future<CrawlJob> crawl(CrawlJob crawlJob, ExecutorService executorService) {
        CompletableFuture<CrawlJob> completableFuture = new CompletableFuture<>();
        crawlJob.setStatus(Status.IN_PROGRESS);

        executorService.submit(() -> {
            crawler.getImgs(crawlJob);
            crawlJob.setStatus(Status.COMPLETE);
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
