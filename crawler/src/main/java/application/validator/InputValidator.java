package application.validator;

import application.exception.InvalidThreadCountException;
import application.exception.InvalidUrlException;
import application.model.InputPayload;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;

public class InputValidator {
    @Autowired
    public InputValidator(){}

    public InputPayload validate(InputPayload inputPayload) {
        if(inputPayload.getThreadCount() <= 0){
            throw new InvalidThreadCountException();
        }
        if(!UrlValidator.getInstance().isValid(inputPayload.getUrl())){
            throw new InvalidUrlException();
        }
        return inputPayload;
    }
}
