package com.fis.agh.gcp.application;

import com.fis.agh.gcp.domain.TodoItemRepository;
import com.fis.agh.gcp.domain.exception.InvalidTodoDtoException;
import com.fis.agh.gcp.domain.exception.ItemNotSavedException;
import com.fis.agh.gcp.domain.TodoItem;
import com.fis.agh.gcp.adapters.pubsub.ConfirmationPublisher;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TodoItemRestService implements TodoItemService {
    private final Logger logger = LoggerFactory.getLogger(TodoItemRestService.class);
    private final TodoItemRepository repository;
    private final TodoItemMapper mapper;
    private final ItemValidator validator;
    private final ConfirmationPublisher publisher;

    public TodoItemDto saveItem(@NotNull TodoItemDto todoItem) {
        ValidationResult validationResult = validator.validateTodoItem(todoItem);
        if (validationResult.validate()) {
            throw new InvalidTodoDtoException(validationResult.getMessages());
        }

        TodoItemDto saved = saveTodoItem(todoItem)
                .map(mapper::mapToDto)
                .orElseThrow(() -> new ItemNotSavedException("Could not save given dto"));

        publisher.publish(saved);
        return saved;
    }

    public List<TodoItemDto> getUserTodoItems(@NotNull QueryItemDto queryItem) {
        ValidationResult validationResult = validator.validateQueryItem(queryItem);
        if (validationResult.validate()) {
            logger.error(validationResult.getMessages());
        }

        return repository.getUserTodoItems(queryItem.getEmailAddress(), queryItem.isCompleted())
                .stream()
                .map(mapper::mapToDto)
                .toList();
    }

    public List<TodoItemDto> queryTodoItems(@NotNull QueryItemDto queryItem) {
        ValidationResult validationResult = validator.validateQueryItem(queryItem);
        if (validationResult.validate()) {
            logger.error(validationResult.getMessages());
        }

        return repository.getTodoItemsByAddressAndDate(queryItem.getEmailAddress(),
                        mapper.mapDateToGoogleTimestamp(queryItem.getDate()))
                .stream()
                .map(mapper::mapToDto)
                .toList();
    }

    public List<TodoItemDto> getItemsWhichShouldBeDone(@NotNull QueryItemDto queryItem) {
        ValidationResult validationResult = validator.validateQueryItem(queryItem);
        if (validationResult.validate()) {
            logger.error(validationResult.getMessages());
        }

        return repository.getTodoItemsToBeDone(queryItem.getEmailAddress(),
                        mapper.mapDateToGoogleTimestamp(queryItem.getDate()))
                .stream()
                .map(mapper::mapToDto)
                .toList();
    }

    public void deleteAllUserTodoItems(@NotNull QueryItemDto queryItem) {
        ValidationResult validationResult = validator.validateQueryItem(queryItem);
        if (validationResult.validate()) {
            logger.error(validationResult.getMessages());
        }

        List<TodoItem> items = repository.getAllByEmailAddress(queryItem.getEmailAddress());
        deleteListOfTodoItems(items);
    }

    public void deleteDoneTodoItems(@NotNull QueryItemDto queryItem) {
        ValidationResult validationResult = validator.validateQueryItem(queryItem);
        if (validationResult.validate()) {
            logger.error(validationResult.getMessages());
        }

        List<TodoItem> items = repository.getUserTodoItems(queryItem.getEmailAddress(), true);
        deleteListOfTodoItems(items);
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
