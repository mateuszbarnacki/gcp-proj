package com.fis.agh.gcp.exception;

import com.fis.agh.gcp.exception.handler.RestStatusCode;

public class InvalidTodoDtoException extends TodoItemException {
    public InvalidTodoDtoException(String message) {
        super(message, RestStatusCode.BAD_REQUEST);
    }
}
