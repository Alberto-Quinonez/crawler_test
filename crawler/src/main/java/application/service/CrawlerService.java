package application.service;

import application.cache.CacheSupplier;
import application.exception.InvalidJobIdException;
import application.job.CrawlJob;
import application.job.Job;
import application.model.InputPayload;
import application.model.ProgressResponse;
import application.model.ResponseFactory;
import application.model.ResultResponse;
import application.scheduler.CrawlScheduler;
import com.google.common.collect.Sets;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CrawlerService {
    private final CacheSupplier cacheSupplier;
    private final CrawlScheduler crawlScheduler;
    private final ResponseFactory responseFactory;

    public UUID crawl(InputPayload inputPayload) {
        Set<CrawlJob> crawlJobs = Sets.newHashSet();
        inputPayload.getUrls().forEach(url -> crawlJobs.add(new CrawlJob(url)));

        UUID jobId = UUID.randomUUID();
        Job job = new Job(jobId, crawlJobs);
        cacheSupplier.get().add(job.getId(), job);
        crawlScheduler.schedule(job, inputPayload.getThreadCount());
        return jobId;
    }

    public ProgressResponse getProgress(UUID jobId) {
        if(!cacheSupplier.get().has(jobId)){
            throw new InvalidJobIdException();
        }
        return responseFactory.getProgress(cacheSupplier.get().get(jobId));
    }

    public ResultResponse getResult(UUID jobId) {
        if(!cacheSupplier.get().has(jobId)){
            throw new InvalidJobIdException();
        }
        return responseFactory.getResult(cacheSupplier.get().get(jobId));
    }
}
