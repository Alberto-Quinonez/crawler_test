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
    public ProgressResponse getProgress(Job job) {
        Map<UUID, CrawlJobStatus> map = Maps.newHashMap();
        int completed = 0;
        int inProgress = 0;

        for(CrawlJob cj : job.getCrawlJobs()){
            CrawlJobStatus status = new CrawlJobStatus(cj.getUrl(), cj.getStatus());
            if(status.getStatus().equals(Status.COMPLETE)) {
                completed += 1;
            }
            if(status.getStatus().equals(Status.IN_PROGRESS)) {
                inProgress += 1;
            }
            map.put(cj.getId(), status);
        }
        return ProgressResponse
                .builder()
                .completed(completed)
                .inProgress(inProgress)
                .map(map)
                .build();
    }

    public ResultResponse getResult(Job job) {
        return ResultResponse
                .builder()
                .crawlJobs(job.getCrawlJobs())
                .build();
    }
}
