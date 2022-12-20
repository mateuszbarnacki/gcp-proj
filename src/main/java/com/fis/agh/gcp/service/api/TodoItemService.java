package com.fis.agh.gcp.service.api;

import com.fis.agh.gcp.dto.QueryItemDto;
import com.fis.agh.gcp.dto.TodoItemDto;

import java.util.Collection;

public interface TodoItemService {

    /**
     * This method saves the given item in database.
     *
     * @param todoItem Item which is saved in database.
     * @return Saved item.
     */
    TodoItemDto saveItem(TodoItemDto todoItem);

    /**
     * This method retrieves all items created by user with emailAddress specified in
     * QueryItemDto object.
     *
     * @param queryItem Object which contains emailAddress of item author.
     * @return Collection of items created by given user.
     */
    Collection<TodoItemDto> getUserTodoItems(QueryItemDto queryItem);

    /**
     * This method retrieves items filtered by emailAddress, date and completed
     * parameters specified in QueryItemDto object.
     *
     * @param queryItem Object which contains filter parameters.
     * @return Collection of items filtered by given parameters.
     */
    Collection<TodoItemDto> queryTodoItems(QueryItemDto queryItem);

    /**
     * This method retrieves not completed items for given user specified by emailAddress.
     *
     * @param queryItem Object which contains emailAddress of item author.
     * @return Collection of items created by given author which should be completed.
     */
    Collection<TodoItemDto> getItemsWhichShouldBeDone(QueryItemDto queryItem);

    /**
     * This method removes all items created by user with given email address.
     *
     * @param queryItem Object which contains emailAddress of item author.
     */
    void deleteAllUserTodoItems(QueryItemDto queryItem);

    /**
     * This method removes only items which are completed by given user.
     *
     * @param queryItem Object which contains emailAddress of item author.
     */
    void deleteDoneTodoItems(QueryItemDto queryItem);
}
