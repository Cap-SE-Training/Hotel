package com.capgemini.setrack.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class InvalidModelException extends ResponseException {

    public InvalidModelException() {
    }

    public InvalidModelException(Exception e){
        super(e);
    }
}