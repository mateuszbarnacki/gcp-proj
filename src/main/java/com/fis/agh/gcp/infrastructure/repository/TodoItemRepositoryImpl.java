package com.fis.agh.gcp.infrastructure.repository;

import com.fis.agh.gcp.domain.TodoItem;
import com.fis.agh.gcp.domain.TodoItemRepository;
import com.google.cloud.Timestamp;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TodoItemRepositoryImpl implements TodoItemRepository {

    private final TodoItemJpaRepository jpaRepository;

    @Override
    public TodoItem save(TodoItem item) {
        return jpaRepository.save(item);
    }

    @Override
    public List<TodoItem> getUserTodoItems(String emailAddress, boolean completed) {
        return jpaRepository.getUserTodoItems(emailAddress, completed);
    }

    @Override
    public List<TodoItem> getTodoItemsByAddressAndDate(String emailAddress, Timestamp date) {
        return jpaRepository.getTodoItemsByAddressAndDate(emailAddress, date);
    }

    @Override
    public List<TodoItem> getTodoItemsToBeDone(String emailAddress, Timestamp date) {
        return jpaRepository.getTodoItemsToBeDone(emailAddress, date);
    }

    @Override
    public List<TodoItem> getAllByEmailAddress(String emailAddress) {
        return jpaRepository.getAllByEmailAddress(emailAddress);
    }

    @Override
    public void deleteAll(List<TodoItem> items) {
        jpaRepository.deleteAll(items);
    }
}
