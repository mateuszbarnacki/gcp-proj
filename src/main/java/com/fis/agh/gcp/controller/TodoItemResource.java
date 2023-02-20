package com.fis.agh.gcp.controller;

import com.fis.agh.gcp.service.TodoItemDto;
import com.fis.agh.gcp.service.TodoItemService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/item")
@RequiredArgsConstructor
public class TodoItemResource {
    private final TodoItemService service;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    @ApiOperation(value = "Publish Pubsub message which triggers cloud function",
            notes = "Method consumes JSON which contains TodoItem data.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Item published successfully"),
            @ApiResponse(code = 400, message = "Invalid TodoItem object"),
            @ApiResponse(code = 500, message = "Could not publish item")})
    public ResponseEntity<String> publishTodoItem(@RequestBody TodoItemDto dto) {
        service.publishItem(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
