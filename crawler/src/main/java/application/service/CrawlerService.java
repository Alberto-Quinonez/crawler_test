package application.service;

import application.cache.JobsDao;
import application.job.CrawlJob;
import application.job.Job;
import application.model.InputPayload;
import application.scheduler.CrawlScheduler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CrawlerService {
    private final JobsDao jobsDao;
    private final CrawlScheduler crawlScheduler;

    @SuppressWarnings("unused")
    @PostConstruct
    protected void init() {
        crawlScheduler.init();
    }

    public UUID schedule(InputPayload inputPayload) {
        Set<CrawlJob> crawlJobs = inputPayload.getUrls().stream()
                .map(CrawlJob::new)
                .collect(Collectors.toSet());

        Job job = new Job(UUID.randomUUID(), crawlJobs);
        jobsDao.save(job);
        crawlScheduler.schedule(job, inputPayload.getThreadCount());

        return job.getId();
    }

    public Optional<Job> get(UUID jobId) {
        return jobsDao.find(jobId);
    }
}
