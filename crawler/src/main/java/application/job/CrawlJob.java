package application.job;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class CrawlJob {
    private final String url;
    private List<String> results;
    private Status status;

    @Override
    public String toString() {
        return "CrawlJob{" +
                "url='" + url + '\'' +
                ", results=" + results +
                ", status=" + status +
                '}';
    }
}
