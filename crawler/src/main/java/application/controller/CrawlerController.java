package application.controller;

import application.model.InputPayload;
import application.service.CrawlerService;
import application.validator.InputValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CrawlerController {
    private static final String BASE_PATH = "/crawler";
    private static final String CRAWL = BASE_PATH + "/crawl";
    private static final String START = BASE_PATH + "/start";
    private static final String PROGRESS = BASE_PATH + "/progress";
    private static final String RESULT = BASE_PATH + "/result";

    private final CrawlerService service;
    private final InputValidator validator;

    @Autowired
    public CrawlerController(CrawlerService service, InputValidator validator) {
        this.service = service;
        this.validator = validator;
    }

    @PostMapping(value = CRAWL)
    @ResponseBody
    public List<String> crawl(@RequestBody InputPayload inputPayload) {
        return service.crawl(validator.validate(inputPayload));
    }

    @PostMapping(value = START)
    @ResponseBody
    public String startThread(@RequestBody int numberThreads) {
        return service.startThread(numberThreads);
    }

    @PostMapping(value = PROGRESS)
    @ResponseBody
    public String getProgress(@RequestBody int numberThreads) {
        return service.startThread(numberThreads);
    }

    @PostMapping(value = RESULT)
    @ResponseBody
    public List<String> startThread(@RequestBody String jobId) {
        return service.getResult(jobId);
    }

}
