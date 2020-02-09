package application.job;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class Job {
    private final UUID id;
    private List<CrawlJob> crawlJobs;

    @Override
    public String toString() {
        return "Job{" +
                "id=" + id +
                ", crawlJobs=" + crawlJobs +
                '}';
    }
}
