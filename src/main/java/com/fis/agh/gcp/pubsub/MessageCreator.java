package com.fis.agh.gcp.pubsub;

import com.fis.agh.gcp.service.TodoItemDto;
import com.google.protobuf.ByteString;

import java.text.DateFormat;

public class MessageCreator {
    private static final String MESSAGE_CONTENT = "The new todo item \"%s\" is created. It's due date is set to %s.";
    private final DateFormat formatter;

    public MessageCreator(DateFormat formatter) {
        this.formatter = formatter;
    }

    public ByteString create(TodoItemDto todo) {
        String formattedDate = formatter.format(todo.getDate());
        return ByteString.copyFromUtf8(buildMessageContent(todo, formattedDate));
    }

    private String buildMessageContent(TodoItemDto todo, String formattedDate) {
        return todo.getAddress() + ";" + todo.getTitle() + " event created;" +
                String.format(MESSAGE_CONTENT, todo.getTitle(), formattedDate);
    }
}
