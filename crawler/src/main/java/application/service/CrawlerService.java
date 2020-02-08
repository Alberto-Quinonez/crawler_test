package application.service;

import application.crawler.Crawler;
import application.model.InputPayload;
import application.model.Progress;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CrawlerService {
    private final Crawler crawler;

    @Autowired
    public CrawlerService(Crawler crawler) {
        this.crawler = crawler;
    }

    public List<String> crawl(InputPayload inputPayload) {
        return crawler.getImgs(inputPayload.getUrl());
    }

    public String startThread(int numThread) {
        return "jobId1";
    }

    public Progress getProgress(String jobId) {
        return new Progress("1", "1");
    }

    public List<String> getResult(String jobId) {
        return Lists.newArrayList("1");
    }
}
