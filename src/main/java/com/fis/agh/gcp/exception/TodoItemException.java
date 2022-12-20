package com.fis.agh.gcp.exception;

import com.fis.agh.gcp.exception.handler.RestStatusCode;

public class TodoItemException extends RuntimeException {
    private final RestStatusCode statusCode;

    public TodoItemException(String message, RestStatusCode statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public RestStatusCode getStatusCode() {
        return this.statusCode;
    }
}
