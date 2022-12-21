package com.fis.agh.gcp.controller.api;

import com.fis.agh.gcp.dto.QueryItemDto;
import com.fis.agh.gcp.dto.TodoItemDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/item")
public interface TodoItemResource {

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Retrieves TodoItems created specified user",
            notes = "Method retrieves TodoItems created by user passed in emailAddress field in QueryItemDtoObject")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Invalid QueryItemDto structure"),
            @ApiResponse(code = 500, message = "Internal server error")})
    ResponseEntity<List<TodoItemDto>> getUserTodoItems(QueryItemDto queryItemDto);

    @GetMapping(value = "/query", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Query TodoItems",
            notes = "Method returns the list of TodoItems based on conditions passed in QueryItemDto object")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Items returned successfully"),
            @ApiResponse(code = 400, message = "Invalid QueryItemDto structure"),
            @ApiResponse(code = 500, message = "Internal server error")})
    ResponseEntity<List<TodoItemDto>> queryTodoItems(QueryItemDto queryItemDto);

    @GetMapping(value = "/done", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Retrieves TodoItems which should be done",
            notes = "Method retrieves TodoItems which should be done by specified user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Items returned successfully"),
            @ApiResponse(code = 400, message = "Invalid QueryItemDto structure"),
            @ApiResponse(code = 500, message = "Internal server error")})
    ResponseEntity<List<TodoItemDto>> getItemsWhichShouldBeDone(QueryItemDto queryItemDto);

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Creates new TodoItem instance",
            notes = "Method consumes JSON which contains TodoItem data.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 201, message = "Item created successfully"),
            @ApiResponse(code = 400, message = "Invalid TodoItem object"),
            @ApiResponse(code = 500, message = "Could not save item in database")})
    ResponseEntity<TodoItemDto> createTodoItem(TodoItemDto dto);

    @DeleteMapping(value = "/all", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Removes all TodoItems",
            notes = "Method removes all TodoItems created by specified user.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 204, message = "Item deleted successfully"),
            @ApiResponse(code = 400, message = "Invalid TodoItem object"),
            @ApiResponse(code = 500, message = "Internal server error")})
    ResponseEntity<Void> deleteAllUserTodoItems(QueryItemDto queryItemDto);

    @DeleteMapping(value = "/done", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Removes TodoItems which are completed",
            notes = "Method removes TodoItems created by specified user which are completed.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 204, message = "Item deleted successfully"),
            @ApiResponse(code = 400, message = "Invalid TodoItem object"),
            @ApiResponse(code = 500, message = "Internal server error")})
    ResponseEntity<Void> deleteDoneTodoItems(QueryItemDto queryItemDto);
}
