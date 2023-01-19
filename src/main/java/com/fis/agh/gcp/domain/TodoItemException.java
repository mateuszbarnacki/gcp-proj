package com.fis.agh.gcp.domain;

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
