package ru.fenix2k.springwebfluxdemo.Exceptions;

import org.springframework.http.HttpStatus;

import java.util.Map;

public class ValidationException extends RuntimeException {

    private HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
    private Map<String, String> validationErrors;

    public ValidationException(Map<String, String> validationErrors) {
        this.validationErrors = validationErrors;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public Map<String, String> getValidationErrors() {
        return validationErrors;
    }

}
