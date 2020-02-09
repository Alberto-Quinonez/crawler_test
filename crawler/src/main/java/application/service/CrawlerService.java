package application.service;

import application.cache.CacheSupplier;
import application.cache.SaveProcess;
import application.crawler.Crawler;
import application.job.CrawlJob;
import application.job.Job;
import application.job.Status;
import application.model.InputPayload;
import application.scheduler.CrawlScheduler;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CrawlerService {
    private final Crawler crawler;
    private final CacheSupplier cacheSupplier;
    private final CrawlScheduler crawlScheduler;
    private final SaveProcess saveProcess;

    public UUID crawl(InputPayload inputPayload, int numThreads) {
        List<CrawlJob> crawlJobs = Lists.newArrayList();
        inputPayload.getUrls().forEach(url -> crawlJobs.add(new CrawlJob(url, Lists.newArrayList(), Status.NOT_STARTED)));

        UUID jobId = UUID.randomUUID();
        Job job = new Job(jobId, crawlJobs);

        saveProcess.save(job);
        crawlScheduler.schedule(job, numThreads);
        return jobId;
    }

    public String getProgress(UUID jobId) {
        return cacheSupplier.get().get(jobId).toString();
    }

    public String getResult(UUID jobId) {
        return cacheSupplier.get().get(jobId).getCrawlJobs().toString();
    }
}
