package com.fis.agh.gcp.service;

import com.fis.agh.gcp.TodoItemMapper;
import com.fis.agh.gcp.dto.TodoItemDto;
import com.fis.agh.gcp.repository.TodoItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TodoItemService {
    private final TodoItemRepository repository;
    private final TodoItemMapper mapper;

    public TodoItemDto saveItem(TodoItemDto dto) {
        throw new UnsupportedOperationException();
    }

    public Optional<TodoItemDto> getItem(Long id) {
        return Optional.of(id)
                .map(repository::findById)
                .map(mapper::mapToDto);
    }

    public TodoItemDto updateItem(TodoItemDto dto) {
        throw new UnsupportedOperationException();
    }

    public void deleteItem(Long id) {
        this.repository.deleteById(id);
    }
}
