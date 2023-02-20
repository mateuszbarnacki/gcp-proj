package com.fis.agh.gcp.exception;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RestError {
    private final String message;
    private final RestStatusCode code;
}
