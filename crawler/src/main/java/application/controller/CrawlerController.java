package application.controller;

import application.exception.InvalidJobIdException;
import application.model.InputPayload;
import application.model.ProgressResponse;
import application.model.ResponseFactory;
import application.model.ResultResponse;
import application.service.CrawlerService;
import application.validator.InputValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/crawler")
public class CrawlerController {
    private final CrawlerService service;
    private final InputValidator validator;
    private final ResponseFactory responseFactory;

    @PostMapping(value = "/crawl")
    @ResponseBody
    public String crawl(@RequestBody InputPayload inputPayload) {
        return service.schedule(validator.validate(inputPayload)).toString();
    }

    @GetMapping(value = "/{jobId}/progress")
    @ResponseBody
    public ProgressResponse getProgress(@PathVariable UUID jobId) {
        return service.get(jobId)
                .map(responseFactory::createProgress)
                .orElseThrow(InvalidJobIdException::new);
    }

    @GetMapping(value = "/{jobId}/result")
    @ResponseBody
    public ResultResponse getResult(@PathVariable UUID jobId) {
        return service.get(jobId)
                .map(responseFactory::createResult)
                .orElseThrow(InvalidJobIdException::new);
    }

}
