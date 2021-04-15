package ru.fenix2k.springwebfluxdemo.Configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import reactor.core.publisher.Mono;
import ru.fenix2k.springwebfluxdemo.Exceptions.Messages.ErrorMessage;
import ru.fenix2k.springwebfluxdemo.Exceptions.ResourceException;
import ru.fenix2k.springwebfluxdemo.Exceptions.ValidationException;
import ru.fenix2k.springwebfluxdemo.Exceptions.Messages.ValidationResultMessage;

@ControllerAdvice
@Slf4j
public class ExceptionHandlerAdvice {

    @ExceptionHandler(ResourceException.class)
    public Mono<ResponseEntity> handleException(ResourceException e) {
        log.debug("ResourceException: {} {}", e.getHttpStatus(), e.getMessage());
        return Mono.just(
                ResponseEntity
                .status(e.getHttpStatus())
                .body(new ErrorMessage(e.getHttpStatus().value(), e.getMessage())));
    }

    @ExceptionHandler(ValidationException.class)
    public Mono<ResponseEntity> handleException(ValidationException e) {
        log.debug("ResourceException: {} {}", e.getHttpStatus(), e.getMessage());
        return Mono.just(
                ResponseEntity
                .status(e.getHttpStatus())
                .body(new ValidationResultMessage(e.getValidationErrors())));
    }

}
