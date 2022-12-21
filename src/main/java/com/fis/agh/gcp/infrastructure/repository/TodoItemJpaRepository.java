package com.fis.agh.gcp.infrastructure.repository;

import com.fis.agh.gcp.domain.TodoItem;

import com.google.cloud.Timestamp;
import org.springframework.cloud.gcp.data.datastore.repository.DatastoreRepository;
import org.springframework.cloud.gcp.data.datastore.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoItemJpaRepository extends DatastoreRepository<TodoItem, Long> {

    List<TodoItem> getAllByAddress(String address);

    @Query("SELECT * FROM todo_items WHERE email_address = @address AND completed = @completed")
    List<TodoItem> getUserTodoItems(@Param("address") String address,
                                    @Param("completed") boolean completed);

    @Query("SELECT * FROM todo_items WHERE email_address = @address AND date = @date")
    List<TodoItem> getTodoItemsByAddressAndDate(@Param("address") String address,
                                                @Param("date") Timestamp date);

    @Query("SELECT * FROM todo_items WHERE email_address = @address AND date >= DATETIME(@date) AND completed = FALSE")
    List<TodoItem> getTodoItemsToBeDone(@Param("address") String address,
                                        @Param("date") Timestamp date);
}
