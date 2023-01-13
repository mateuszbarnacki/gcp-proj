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
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class TodoItemRestService implements TodoItemService {
    private final Logger logger = LoggerFactory.getLogger(TodoItemRestService.class);
    private static final String GMAIL_DOMAIN = "@gmail.com";
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

    public List<TodoItemDto> getUserTodoItems(String user) {
        String emailAccount = user + GMAIL_DOMAIN;

        return repository.getAllByEmailAddress(emailAccount)
                .stream()
                .map(mapper::mapToDto)
                .collect(Collectors.toList());
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
