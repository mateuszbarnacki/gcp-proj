package com.fis.agh.gcp.service.impl;

import com.fis.agh.gcp.dto.QueryItemDto;
import com.fis.agh.gcp.exception.InvalidTodoDateException;
import com.fis.agh.gcp.exception.ItemNotSavedException;
import com.fis.agh.gcp.mapper.TodoItemMapper;
import com.fis.agh.gcp.dto.TodoItemDto;
import com.fis.agh.gcp.model.TodoItem;
import com.fis.agh.gcp.pubsub.ConfirmationPublisher;
import com.fis.agh.gcp.repository.TodoItemRepository;
import com.fis.agh.gcp.service.api.TodoItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TodoItemRestService implements TodoItemService {
    private final TodoItemRepository repository;
    private final TodoItemMapper mapper;
    private final ConfirmationPublisher publisher;

    public TodoItemDto saveItem(@NotNull TodoItemDto todoItem) {
        validateDate(todoItem);
        publisher.publish(todoItem);
        return saveTodoItem(todoItem)
                .map(mapper::mapToDto)
                .orElseThrow(() -> new ItemNotSavedException("Could not save given dto"));
    }

    public List<TodoItemDto> getUserTodoItems(@NotNull QueryItemDto queryItem) {
        return repository.getUserTodoItems(queryItem.getEmailAddress(), queryItem.isCompleted())
                .stream()
                .map(mapper::mapToDto)
                .toList();
    }

    public List<TodoItemDto> queryTodoItems(@NotNull QueryItemDto queryItem) {
        return repository.getTodoItemsByAddressAndDate(queryItem.getEmailAddress(),
                        mapper.mapDateToGoogleTimestamp(queryItem.getDate()))
                .stream()
                .map(mapper::mapToDto)
                .toList();
    }

    public List<TodoItemDto> getItemsWhichShouldBeDone(@NotNull QueryItemDto queryItem) {
        return repository.getTodoItemsToBeDone(queryItem.getEmailAddress(),
                        mapper.mapDateToGoogleTimestamp(queryItem.getDate()))
                .stream()
                .map(mapper::mapToDto)
                .toList();
    }

    public void deleteAllUserTodoItems(@NotNull QueryItemDto queryItem) {
        List<TodoItem> items = repository.getAllByAddress(queryItem.getEmailAddress());
        deleteListOfTodoItems(items);
    }

    public void deleteDoneTodoItems(@NotNull QueryItemDto queryItem) {
        List<TodoItem> items = repository.getUserTodoItems(queryItem.getEmailAddress(), true);
        deleteListOfTodoItems(items);
    }

    private void validateDate(TodoItemDto dto) {
        if (dto.getDate().compareTo(Date.from(Instant.now())) < 0) {
            throw new InvalidTodoDateException("Given date is in the past!");
        }
    }

    private Optional<TodoItem> saveTodoItem(TodoItemDto dto) {
        TodoItem item = mapper.mapToEntity(dto);
        TodoItem newItem = repository.save(item);
        return Optional.of(newItem);
    }

    private void deleteListOfTodoItems(List<TodoItem> items) {
        repository.deleteAll(items);
    }
}
