package com.fis.agh.gcp.infrastructure.exception;

import com.fis.agh.gcp.domain.exception.InvalidTodoDateException;
import com.fis.agh.gcp.domain.exception.ItemNotFoundException;
import com.fis.agh.gcp.domain.exception.ItemNotSavedException;
import com.fis.agh.gcp.domain.exception.TodoItemException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestApiErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({InvalidTodoDateException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<RestError> handleInvalidTodoDateException(InvalidTodoDateException e) {
        return new ResponseEntity<>(buildRestError(e), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ItemNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<RestError> handleItemNotFoundException(ItemNotFoundException e) {
        return new ResponseEntity<>(buildRestError(e), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({ItemNotSavedException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<RestError> handleItemNotSavedException(ItemNotSavedException e) {
        return new ResponseEntity<>(buildRestError(e), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private RestError buildRestError(TodoItemException e) {
        return RestError.builder()
                .message(e.getMessage())
                .code(e.getStatusCode())
                .build();
    }
}
