package com.fis.agh.gcp.domain.exception;

public class InvalidTodoDateException extends TodoItemException {
    public InvalidTodoDateException(String message) {
        super(message, RestStatusCode.BAD_REQUEST);
    }
}
