package com.fis.agh.gcp.application;

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
     * @param user The first part of gmail email address.
     * @return Collection of items created by given user.
     */
    Collection<TodoItemDto> getUserTodoItems(String user);

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
