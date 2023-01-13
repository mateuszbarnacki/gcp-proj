package com.fis.agh.gcp.domain;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface TodoItemRepository {

    TodoItem save(TodoItem item);

    List<TodoItem> getUserTodoItems(String emailAddress, boolean completed);

    List<TodoItem> getAllByEmailAddress(String emailAddress);

    void deleteAll(List<TodoItem> items);
}
