package com.fis.agh.gcp.domain;

import com.google.cloud.Timestamp;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface TodoItemRepository {

    TodoItem save(TodoItem item);

    List<TodoItem> getUserTodoItems(String emailAddress, boolean completed);

    List<TodoItem> getTodoItemsByAddressAndDate(String emailAddress, Timestamp date);

    List<TodoItem> getTodoItemsToBeDone(String emailAddress, Timestamp date);

    List<TodoItem> getAllByAddress(String emailAddress);

    void deleteAll(List<TodoItem> items);
}
