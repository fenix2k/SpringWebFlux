package ru.fenix2k.springwebfluxdemo.Exceptions.Messages;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
public class ErrorMessage {

    /** Timestamp */
    private  long timestamp;
    /** HTTP status */
    private int status;
    /** Error message */
    private String message;

    public ErrorMessage(int status, String message, long timestamp) {
        this.status = status;
        this.message = message;
        this.timestamp = timestamp;
    }

    public ErrorMessage(int status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = new Timestamp(System.currentTimeMillis()).getTime();
    }

}
