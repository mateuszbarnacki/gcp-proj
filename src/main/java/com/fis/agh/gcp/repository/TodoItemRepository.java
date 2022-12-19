package com.fis.agh.gcp.repository;

import com.fis.agh.gcp.model.TodoItem;

import org.springframework.cloud.gcp.data.datastore.repository.DatastoreRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoItemRepository extends DatastoreRepository<TodoItem, Long> {

}
