package application.model;

import application.job.Status;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@ToString
@Getter
class CrawlJobStatus {
    private final String url;
    private final Status status;
}
