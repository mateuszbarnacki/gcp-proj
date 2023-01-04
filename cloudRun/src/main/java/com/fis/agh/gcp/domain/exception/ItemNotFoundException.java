package com.fis.agh.gcp.domain.exception;

public class ItemNotFoundException extends TodoItemException {
    public ItemNotFoundException(String message) {
        super(message, RestStatusCode.NOT_FOUND);
    }
}
