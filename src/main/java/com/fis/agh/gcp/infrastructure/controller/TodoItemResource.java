package com.fis.agh.gcp.infrastructure.controller;

import com.fis.agh.gcp.application.TodoItemDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/item")
@Tag(name = "TodoItem", description = "This resource handle todo item operations")
public interface TodoItemResource {

    @Operation(summary = "Publish PubSub message which trigger cloud function", responses = {
            @ApiResponse(responseCode = "201", description = "Message published successfully"),
            @ApiResponse(
                    responseCode = "400",
                    description = "The request was not valid.",
                    content = @Content(
                            examples = {@ExampleObject(
                                    value = "{\"message\":\"Invalid email address structure!\", \"code\":\"BAD_REQUEST\"}"
                            )},
                            mediaType = MediaType.APPLICATION_JSON_VALUE))
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    ResponseEntity<String> createTodoItem(TodoItemDto dto);
}
