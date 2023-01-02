package com.fis.agh.gcp.infrastructure.repository;

import com.fis.agh.gcp.domain.TodoItem;

import com.google.cloud.Timestamp;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoItemJpaRepository extends CrudRepository<TodoItem, Long> {

    List<TodoItem> getAllByEmailAddress(String address);

    @Query("SELECT ti FROM todo_items ti WHERE ti.emailAddress = :address AND ti.completed = :completed")
    List<TodoItem> getUserTodoItems(@Param("address") String address,
                                    @Param("completed") boolean completed);

    @Query("SELECT ti FROM todo_items ti WHERE ti.emailAddress = :address AND ti.date = :date")
    List<TodoItem> getTodoItemsByAddressAndDate(@Param("address") String address,
                                                @Param("date") Timestamp date);

    @Query("SELECT ti FROM todo_items ti WHERE ti.emailAddress = :address AND ti.date >= DATETIME(:date) AND ti.completed = FALSE")
    List<TodoItem> getTodoItemsToBeDone(@Param("address") String address,
                                        @Param("date") Timestamp date);
}
