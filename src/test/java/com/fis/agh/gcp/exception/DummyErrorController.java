package com.fis.agh.gcp.exception;

import com.fis.agh.gcp.exception.handler.RestError;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/error")
public class DummyErrorController {

    @GetMapping("/invalid-date")
    public ResponseEntity<RestError> dummyInvalidDateController() {
        throw new InvalidTodoDateException("Invalid date!");
    }

    @GetMapping("/invalid-dto")
    public ResponseEntity<RestError> dummyInvalidDtoController() {
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
