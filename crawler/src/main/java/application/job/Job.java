package application.job;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@Getter
@ToString
public class Job {
    private final UUID id;
    private Set<CrawlJob> crawlJobs;
}
