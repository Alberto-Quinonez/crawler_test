package application.validator;

import application.exception.InvalidThreadCountException;
import application.exception.InvalidUrlException;
import application.model.InputPayload;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.stereotype.Component;

@Component
public class InputValidator {
    public InputPayload validate(InputPayload inputPayload) {
        if(inputPayload.getThreadCount() <= 0){
            throw new InvalidThreadCountException();
        }
        if(inputPayload.getUrls() == null || inputPayload.getUrls().isEmpty()){
            throw new InvalidUrlException();
        }
        inputPayload.getUrls()
                .forEach(ip -> {
                    if(!UrlValidator.getInstance().isValid(ip)){
                        throw new InvalidUrlException();
                }
        });
        return inputPayload;
    }
}
