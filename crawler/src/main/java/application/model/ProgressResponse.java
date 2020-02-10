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
    private int completed;
    private int inProgress;
    private Map<UUID, CrawlJobStatus> map;
}
