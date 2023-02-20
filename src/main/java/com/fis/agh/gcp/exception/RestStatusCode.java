package com.fis.agh.gcp.exception;

public enum RestStatusCode {
    BAD_REQUEST(400),
    NOT_FOUND(404),
    INTERNAL_SERVER_ERROR(500);

    RestStatusCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    private final int code;
}
