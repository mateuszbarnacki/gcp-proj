package com.fis.agh.gcp.controller;

import com.fis.agh.gcp.dto.TodoItemDto;
import com.fis.agh.gcp.service.TodoItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/todo")
@RequiredArgsConstructor
public class TodoItemResource {
    private final TodoItemService service;

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TodoItemDto> getTodoItem(@PathVariable Long id) {
        return service.getItem(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new IllegalArgumentException("Invalid id"));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TodoItemDto> createTodoItem(@RequestBody TodoItemDto dto) {
        return ResponseEntity.ok(service.saveItem(dto));
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TodoItemDto> updateTodoItem(@RequestBody TodoItemDto dto) {
        return ResponseEntity.ok(service.updateItem(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodoItem(@PathVariable Long id) {
        service.deleteItem(id);
        return ResponseEntity.ok().build();
    }

}
