package com.fis.agh.gcp.infrastructure.exception;

import com.fis.agh.gcp.domain.exception.RestStatusCode;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RestError {
    private final String message;
    private final RestStatusCode code;
}
