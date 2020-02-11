package application.job;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
@Getter
@ToString
public class Job {
    private final UUID id;
    private final Set<CrawlJob> crawlJobs;
}
