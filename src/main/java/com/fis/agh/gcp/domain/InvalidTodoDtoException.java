package com.fis.agh.gcp.domain;

public class InvalidTodoDtoException extends TodoItemException {
    public InvalidTodoDtoException(String message) {
        super(message, RestStatusCode.BAD_REQUEST);
    }
}
