package com.fis.agh.gcp.exception;

import com.fis.agh.gcp.exception.handler.RestStatusCode;

public class ItemNotFoundException extends TodoItemException {
    public ItemNotFoundException(String message) {
        super(message, RestStatusCode.NOT_FOUND);
    }
}
