package com.fis.agh.gcp.controller;

import com.fis.agh.gcp.dto.QueryItemDto;
import com.fis.agh.gcp.dto.TodoItemDto;
import com.fis.agh.gcp.service.TodoItemRestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/item")
@RequiredArgsConstructor
public class TodoItemResource {
    private final TodoItemRestService service;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TodoItemDto>> getTodoItem(@RequestBody QueryItemDto queryItemDto) {
        return ResponseEntity.ok(service.getUserTodoItems(queryItemDto));
    }

    @GetMapping(value = "/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TodoItemDto>> queryTodoItems(@RequestBody QueryItemDto queryItemDto) {
        return ResponseEntity.ok(service.queryTodoItems(queryItemDto));
    }

    @GetMapping(value = "/done", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TodoItemDto>> getItemsWhichShouldBeDone(@RequestBody QueryItemDto queryItemDto) {
        return ResponseEntity.ok(service.getItemsWhichShouldBeDone(queryItemDto));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TodoItemDto> createTodoItem(@RequestBody TodoItemDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.saveItem(dto));
    }

    @DeleteMapping(value = "/all", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteAllUserTodoItems(@RequestBody QueryItemDto queryItemDto) {
        service.deleteAllUserTodoItems(queryItemDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/done", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteDoneTodoItems(@RequestBody QueryItemDto queryItemDto) {
        service.deleteDoneTodoItems(queryItemDto);
        return ResponseEntity.ok().build();
    }

}
