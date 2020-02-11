package application.model;

import application.job.CrawlJob;
import application.job.Job;
import application.job.Status;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Component
public class ResponseFactory {
    public ProgressResponse createProgress(Job job) {
        Map<UUID, CrawlJobStatus> map = Maps.newHashMap();

        int notStarted = 0;
        int inProgress = 0;
        int completed = 0;
        int error = 0;

        for (CrawlJob cj : job.getCrawlJobs()) {
            Status status = cj.getStatus();
            switch (status) {
                case NOT_STARTED:
                    notStarted++;
                    break;
                case IN_PROGRESS:
                    inProgress++;
                    break;
                case COMPLETE:
                    completed++;
                    break;
                case ERROR:
                    error++;
            }

            map.put(cj.getId(), new CrawlJobStatus(cj.getUrl(), status));
        }
        return ProgressResponse
                .builder()
                .total(map.size())
                .notStarted(notStarted)
                .inProgress(inProgress)
                .completed(completed)
                .error(error)
                .crawlJobStatusMap(map)
                .build();
    }

    public ResultResponse createResult(Job job) {
        return ResultResponse
                .builder()
                .crawlJobs(job.getCrawlJobs())
                .build();
    }
}
