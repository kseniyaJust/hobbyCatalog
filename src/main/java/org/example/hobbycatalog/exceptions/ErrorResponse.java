package org.example.hobbycatalog.exceptions;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class ErrorResponse {
    private final int status;
    private final String message;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private final LocalDateTime timestamp;

    public ErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
        timestamp = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "{ " + status + ", \n"
                + message + ",\n "
                + timestamp.toString() + " }";
    }
}
