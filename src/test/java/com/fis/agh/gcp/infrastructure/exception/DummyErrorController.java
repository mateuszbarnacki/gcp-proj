package com.fis.agh.gcp.infrastructure.exception;

import com.fis.agh.gcp.domain.exception.InvalidTodoDtoException;
import com.fis.agh.gcp.domain.exception.ItemNotFoundException;
import com.fis.agh.gcp.domain.exception.ItemNotSavedException;
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

    @GetMapping("/item-not-found")
    public ResponseEntity<RestError> dummyItemNotFoundController() {
        throw new ItemNotFoundException("Not found!");
    }

    @GetMapping("/item-not-saved")
    public ResponseEntity<RestError> dummyItemNotSavedController() {
        throw new ItemNotSavedException("Not saved!");
    }
}
