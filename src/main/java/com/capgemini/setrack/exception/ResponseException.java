package com.capgemini.setrack.exception;

import com.sun.deploy.util.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
public class ResponseException extends Exception {

    private String message;

    ResponseException(){
    }

    ResponseException(ConstraintViolationException e)
    {
        super();
        List<String> errors = new ArrayList<String>();
        for(ConstraintViolation cv: e.getConstraintViolations()){
            errors.add(cv.getMessage());
        }
        this.message = String.join(",<br>", errors);
    }

    @Override
    public String getMessage(){
        return message;
    }

}
