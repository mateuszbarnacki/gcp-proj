package com.fis.agh.gcp.infrastructure.controller;

import com.fis.agh.gcp.application.QueryItemDto;
import com.fis.agh.gcp.application.TodoItemDto;
import com.fis.agh.gcp.application.TodoItemRestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TodoItemResourceImpl implements TodoItemResource {
    private final TodoItemRestService service;

    @Override
    public ResponseEntity<List<TodoItemDto>> getUserTodoItems(@RequestBody QueryItemDto queryItemDto) {
        return ResponseEntity.ok(service.getUserTodoItems(queryItemDto));
    }

    @Override
    public ResponseEntity<List<TodoItemDto>> queryTodoItems(@RequestBody QueryItemDto queryItemDto) {
        return ResponseEntity.ok(service.queryTodoItems(queryItemDto));
    }

    @Override
    public ResponseEntity<List<TodoItemDto>> getItemsWhichShouldBeDone(@RequestBody QueryItemDto queryItemDto) {
        return ResponseEntity.ok(service.getItemsWhichShouldBeDone(queryItemDto));
    }

    @Override
    public ResponseEntity<TodoItemDto> createTodoItem(@RequestBody TodoItemDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.saveItem(dto));
    }

    @Override
    public ResponseEntity<Void> deleteAllUserTodoItems(@RequestBody QueryItemDto queryItemDto) {
        service.deleteAllUserTodoItems(queryItemDto);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> deleteDoneTodoItems(@RequestBody QueryItemDto queryItemDto) {
        service.deleteDoneTodoItems(queryItemDto);
        return ResponseEntity.ok().build();
    }

}
