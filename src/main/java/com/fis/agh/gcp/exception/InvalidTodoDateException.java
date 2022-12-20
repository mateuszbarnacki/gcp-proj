package com.fis.agh.gcp.exception;

import com.fis.agh.gcp.exception.handler.RestStatusCode;

public class InvalidTodoDateException extends TodoItemException {
    public InvalidTodoDateException(String message) {
        super(message, RestStatusCode.BAD_REQUEST);
    }
}
