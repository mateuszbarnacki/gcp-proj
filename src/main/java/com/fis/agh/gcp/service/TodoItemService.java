package com.fis.agh.gcp.service;

import com.fis.agh.gcp.dto.QueryItemDto;
import com.fis.agh.gcp.exception.InvalidTodoDateException;
import com.fis.agh.gcp.exception.ItemNotSavedException;
import com.fis.agh.gcp.mapper.TodoItemMapper;
import com.fis.agh.gcp.dto.TodoItemDto;
import com.fis.agh.gcp.model.TodoItem;
import com.fis.agh.gcp.pubsub.ConfirmationPublisher;
import com.fis.agh.gcp.repository.TodoItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TodoItemService {
    private final TodoItemRepository repository;
    private final TodoItemMapper mapper;
    private final ConfirmationPublisher publisher;

    public TodoItemDto saveItem(TodoItemDto dto) {
        if (dto.getDate().compareTo(Date.from(Instant.now())) < 0) {
            throw new InvalidTodoDateException("Given date is in the past!");
        }

        publisher.publish(dto);
        return Optional.of(dto)
                .map(mapper::mapToEntity)
                .map(repository::save)
                .map(mapper::mapToDto)
                .orElseThrow(() -> new ItemNotSavedException("Could not save given dto"));
    }

    public List<TodoItemDto> getUserTodoItems(QueryItemDto queryItem) {
        return repository.getUserTodoItems(queryItem.getAddress(), queryItem.isCompleted())
                .stream()
                .map(mapper::mapToDto)
                .toList();
    }

    public List<TodoItemDto> queryTodoItems(QueryItemDto queryItem) {
        return repository.getTodoItemsByAddressAndDate(queryItem.getAddress(),
                        mapper.mapDateToGoogleTimestamp(queryItem.getDate())).stream()
                .map(mapper::mapToDto)
                .toList();
    }

    public List<TodoItemDto> getItemsWhichShouldBeDone(QueryItemDto queryItem) {
        return repository.getTodoItemsToBeDone(queryItem.getAddress(),
                        mapper.mapDateToGoogleTimestamp(queryItem.getDate())).stream()
                .map(mapper::mapToDto)
                .toList();
    }

    public void deleteAllUserTodoItems(QueryItemDto queryItem) {
        List<TodoItem> items = repository.getAllByAddress(queryItem.getAddress());
        deleteListOfTodoItems(items);
    }

    public void deleteDoneTodoItems(QueryItemDto queryItem) {
        List<TodoItem> items = repository.getUserTodoItems(queryItem.getAddress(), true);
        deleteListOfTodoItems(items);
    }

    private void deleteListOfTodoItems(List<TodoItem> items) {
        repository.deleteAll(items);
    }
}
