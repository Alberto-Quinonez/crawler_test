package application.controller;

import application.model.InputPayload;
import application.service.CrawlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CrawlerController {
    private static final String BASE_PATH = "/crawler";
    private static final String CRAWL = BASE_PATH + "/crawl";
    private static final String RESULT = BASE_PATH + "/result";

    private final CrawlerService service;

    @Autowired
    public CrawlerController(CrawlerService service) {
        this.service = service;
    }

    @PostMapping(value = CRAWL)
    @ResponseBody
    public List<String> crawl(@RequestBody InputPayload payload) {
        return service.crawl(payload);
    }
}
