package application.job;

import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.Duration;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
@Getter
@Setter
@ToString
public class CrawlJob {
    private final UUID id = UUID.randomUUID();
    private final String url;
    private Status status = Status.NOT_STARTED;
    private String timeElapsed = Duration.ZERO.toString();
    private int resultSize = 0;
    private Set<String> results = Sets.newHashSet();
}
