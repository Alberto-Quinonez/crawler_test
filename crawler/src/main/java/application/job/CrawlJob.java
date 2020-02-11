package application.job;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class CrawlJob {
    private final UUID id = UUID.randomUUID();
    private final String url;

    private Status status = Status.NOT_STARTED;
    private Duration durationInSeconds;
    private Integer resultSize;
    private Set<String> results;

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setCompletedValues(Duration timeElapsed, Set<String> results) {
        this.durationInSeconds = timeElapsed;
        this.results = results;
        this.resultSize = results.size();
        this.status = Status.COMPLETE;
    }
}
