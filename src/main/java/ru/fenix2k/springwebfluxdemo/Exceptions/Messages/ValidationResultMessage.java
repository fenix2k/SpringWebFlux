package ru.fenix2k.springwebfluxdemo.Exceptions.Messages;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.sql.Timestamp;
import java.util.Map;

@Data
@NoArgsConstructor
public class ValidationResultMessage {

    /** Timestamp */
    private  long timestamp;
    /** HTTP status */
    private int status = HttpStatus.BAD_REQUEST.value();
    /** Error message */
    private Map<String, String> validationErrors;


    public ValidationResultMessage(Map<String, String> validationErrors) {
        this.validationErrors = validationErrors;
        this.timestamp = new Timestamp(System.currentTimeMillis()).getTime();
    }

    public ValidationResultMessage(Map<String, String> validationErrors, long timestamp) {
        this.validationErrors = validationErrors;
        this.timestamp = timestamp;
    }
}
