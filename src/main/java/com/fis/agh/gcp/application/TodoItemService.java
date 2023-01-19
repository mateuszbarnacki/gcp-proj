package com.fis.agh.gcp.application;

public interface TodoItemService {

    /**
     * This method saves the given item in database.
     *
     * @param todoItem Item which is saved in database.
     * @return Saved item.
     */
    String saveItem(TodoItemDto todoItem);
}
