package application.validator;

import application.exception.InvalidThreadCountException;
import application.exception.InvalidUrlException;
import application.exception.MaxThreadCountException;
import application.model.InputPayload;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class InputValidator {
    public InputPayload validate(InputPayload inputPayload) {
        if (inputPayload.getThreadCount() < 0) {
            throw new InvalidThreadCountException();
        }

        if (inputPayload.getThreadCount() > Runtime.getRuntime().availableProcessors()) {
            throw new MaxThreadCountException();
        }

        if (CollectionUtils.isEmpty(inputPayload.getUrls())) {
            throw new InvalidUrlException();
        }

        inputPayload.getUrls().forEach(ip -> {
            if (!UrlValidator.getInstance().isValid(ip)) {
                throw new InvalidUrlException();
            }
        });
        return inputPayload;
    }
}
