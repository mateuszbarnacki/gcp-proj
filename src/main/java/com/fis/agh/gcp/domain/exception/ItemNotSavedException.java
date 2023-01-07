package com.fis.agh.gcp.domain.exception;

public class ItemNotSavedException extends TodoItemException {
    public ItemNotSavedException(String message) {
        super(message, RestStatusCode.INTERNAL_SERVER_ERROR);
    }
}
