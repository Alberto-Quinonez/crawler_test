package application.service;

import application.crawler.Crawler;
import application.exception.InvalidThreadCountException;
import application.model.InputPayload;
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
        if(inputPayload.getThread() <= 0){
            throw new InvalidThreadCountException();
        }
        return crawler.getImgs(inputPayload.getUrl());
    }
}
