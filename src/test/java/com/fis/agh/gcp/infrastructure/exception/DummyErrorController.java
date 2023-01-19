package com.fis.agh.gcp.infrastructure.exception;

import com.fis.agh.gcp.domain.InvalidTodoDtoException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/error")
public class DummyErrorController {

    @GetMapping("/invalid-dto")
    public ResponseEntity<RestError> dummyInvalidDateController() {
        throw new InvalidTodoDtoException("Invalid dto!");
    }
}
