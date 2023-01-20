package com.fis.agh.gcp.infrastructure.controller;

import com.fis.agh.gcp.application.TodoItemDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/item")
public interface TodoItemResource {

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    @ApiOperation(value = "Publish Pubsub message which triggers cloud function",
            notes = "Method consumes JSON which contains TodoItem data.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 201, message = "Item published successfully"),
            @ApiResponse(code = 400, message = "Invalid TodoItem object"),
            @ApiResponse(code = 500, message = "Could not publish item")})
    ResponseEntity<String> createTodoItem(TodoItemDto dto);
}
