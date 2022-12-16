package com.fis.agh.gcp.repository;

import com.fis.agh.gcp.model.TodoItem;
import org.springframework.cloud.gcp.data.datastore.repository.DatastoreRepository;

import java.time.ZonedDateTime;
import java.util.List;

public interface TodoItemRepository extends DatastoreRepository<TodoItem, Long> {

    TodoItem findById(Long id);
    List<TodoItem> findByDate(ZonedDateTime date);
    void deleteById(Long id);

}
