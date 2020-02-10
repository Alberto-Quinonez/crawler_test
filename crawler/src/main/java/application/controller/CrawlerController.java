package application.controller;

import application.model.InputPayload;
import application.service.CrawlerService;
import application.validator.InputValidator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class CrawlerController {
    private static final String BASE_PATH = "/crawler";
    private static final String CRAWL = BASE_PATH + "/crawl";
    private static final String PROGRESS = BASE_PATH + "/progress/{jobId}";
    private static final String RESULT = BASE_PATH + "/result/{jobId}";

    private final CrawlerService service;
    private final InputValidator validator;
    private final ObjectMapper objectMapper;

    @PostMapping(value = CRAWL)
    @ResponseBody
    public String crawl(@RequestBody InputPayload inputPayload) throws JsonProcessingException {
        return objectMapper.writeValueAsString(service.crawl(validator.validate(inputPayload)));
    }

    @GetMapping(value = PROGRESS)
    @ResponseBody
    public String getProgress(@PathVariable UUID jobId) throws JsonProcessingException {
        return objectMapper.writeValueAsString(service.getProgress(jobId));
    }

    @GetMapping(value = RESULT)
    @ResponseBody
    public String getResult(@PathVariable UUID jobId) throws JsonProcessingException {
        return objectMapper.writeValueAsString(service.getResult(jobId));
    }

}
