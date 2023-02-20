package com.fis.agh.gcp.service;

public interface TodoItemService {

    /**
     * This method saves the given item in database.
     *
     * @param todoItem Item which is saved in database.
     * @return Saved item.
     */
    String publishItem(TodoItemDto todoItem);
}
