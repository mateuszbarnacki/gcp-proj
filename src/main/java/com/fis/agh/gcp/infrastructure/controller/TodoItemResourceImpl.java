package com.fis.agh.gcp.infrastructure.controller;

import com.fis.agh.gcp.application.TodoItemDto;
import com.fis.agh.gcp.application.TodoItemRestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class TodoItemResourceImpl implements TodoItemResource {
    private final TodoItemRestService service;

    @Override
    public ResponseEntity<String> createTodoItem(@RequestBody TodoItemDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.saveItem(dto));
    }
}
