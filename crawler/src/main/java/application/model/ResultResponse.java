package application.model;

import application.job.CrawlJob;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Builder
@Getter
@Setter
public class ResultResponse {
    private Set<CrawlJob> crawlJobs;
}
