package com.fis.agh.gcp.service;

public interface TodoItemService {

    /**
     * This method publish the given item on pubsub.
     *
     * @param todoItem Item which is converted to message and published on pubsub.
     */
    void publishItem(TodoItemDto todoItem);
}
