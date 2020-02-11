package application.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.UUID;

@Builder
@Getter
@Setter
public class ProgressResponse {
    private int total;
    private int notStarted;
    private int inProgress;
    private int completed;
    private int error;
    private Map<UUID, CrawlJobStatus> crawlJobStatusMap;
}
