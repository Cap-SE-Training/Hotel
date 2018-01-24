package com.capgemini.setrack.exception;

import org.springframework.dao.DataIntegrityViolationException;
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

    ResponseException(Exception e)
    {
        super();

        if(e instanceof ConstraintViolationException) {
            List<String> errors = new ArrayList<String>();
            for (ConstraintViolation cv : ((ConstraintViolationException) e).getConstraintViolations()) {
                errors.add(cv.getMessage());
            }
            this.message = String.join(",<br>", errors);
        } else if(e instanceof DataIntegrityViolationException){
            this.message = "The record is a duplicate entry and could not be added!";
        } else {
            this.message = "Something went wrong";
        }
    }

    @Override
    public String getMessage(){
        return message;
    }

}
