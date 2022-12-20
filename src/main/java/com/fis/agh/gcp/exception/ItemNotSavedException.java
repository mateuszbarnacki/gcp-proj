package com.fis.agh.gcp.exception;

import com.fis.agh.gcp.exception.handler.RestStatusCode;

public class ItemNotSavedException extends TodoItemException {
    public ItemNotSavedException(String message) {
        super(message, RestStatusCode.INTERNAL_SERVER_ERROR);
    }
}
