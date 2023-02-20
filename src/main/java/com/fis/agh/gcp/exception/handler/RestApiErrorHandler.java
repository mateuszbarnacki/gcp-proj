package com.fis.agh.gcp.exception.handler;

import com.fis.agh.gcp.exception.InvalidTodoDtoException;
import com.fis.agh.gcp.exception.RestError;
import com.fis.agh.gcp.exception.TodoItemException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestApiErrorHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler({InvalidTodoDtoException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<RestError> handleInvalidTodoDateException(InvalidTodoDtoException e) {
        return new ResponseEntity<>(buildRestError(e), HttpStatus.BAD_REQUEST);
    }

    private RestError buildRestError(TodoItemException e) {
        return RestError.builder()
                .message(e.getMessage())
                .code(e.getStatusCode())
                .build();
    }
}
