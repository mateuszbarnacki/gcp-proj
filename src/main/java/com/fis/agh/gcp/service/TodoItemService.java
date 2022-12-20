package com.fis.agh.gcp.service;

import com.fis.agh.gcp.mapper.TodoItemMapper;
import com.fis.agh.gcp.dto.TodoItemDto;
import com.fis.agh.gcp.model.TodoItem;
import com.fis.agh.gcp.pubsub.ConfirmationPublisher;
import com.fis.agh.gcp.repository.TodoItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TodoItemService {
    private final TodoItemRepository repository;
    private final TodoItemMapper mapper;
    private final ConfirmationPublisher publisher;

    public TodoItemDto saveItem(TodoItemDto dto) {
        publisher.publish(dto);
        return Optional.of(dto)
                .map(mapper::mapToEntity)
                .map(repository::save)
                .map(mapper::mapToDto)
                .orElseThrow(() -> new RuntimeException("Could not save given dto"));
    }

    public Optional<TodoItemDto> getItem(Long id) {
        return repository.findById(id)
                .map(mapper::mapToDto);
    }

    public TodoItemDto updateItem(Long id, TodoItemDto dto) {
        TodoItem item = repository.findById(id).orElseThrow(() -> new RuntimeException("Could not find item with id=" + id));

        item.setTitle(dto.getTitle());
        item.setContent(dto.getContent());
        //item.setCompleted(dto.isCompleted());

        return Optional.of(item)
                .map(repository::save)
                .map(mapper::mapToDto)
                .orElseThrow(() -> new RuntimeException("Could not save updated item with id=" + id));
    }

    public void deleteItem(Long id) {
        this.repository.deleteById(id);
    }
}
