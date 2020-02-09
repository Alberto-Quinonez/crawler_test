package application.controller;

import application.model.InputPayload;
import application.service.CrawlerService;
import application.validator.InputValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class CrawlerController {
    private static final String BASE_PATH = "/crawler";
    private static final String CRAWL = BASE_PATH + "/crawl";
    private static final String PROGRESS = BASE_PATH + "/progress/{id}";
    private static final String RESULT = BASE_PATH + "/result/{id}";

    private final CrawlerService service;
    private final InputValidator validator;

    @Autowired
    public CrawlerController(CrawlerService service, InputValidator validator) {
        this.service = service;
        this.validator = validator;
    }

    @PostMapping(value = CRAWL)
    @ResponseBody
    public UUID crawl(@RequestBody InputPayload inputPayload, @RequestBody int numThreads) {
        return service.crawl(validator.validate(inputPayload), numThreads);
    }

    @GetMapping(value = PROGRESS)
    @ResponseBody
    public String getProgress(@PathVariable UUID jobId) {
        return service.getProgress(jobId);
    }

    @GetMapping(value = RESULT)
    @ResponseBody
    public String getResult(@PathVariable UUID jobId) {
        return service.getResult(jobId);
    }

}
