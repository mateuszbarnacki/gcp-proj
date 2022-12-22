package com.fis.agh.gcp.adapters.pubsub;

import com.fis.agh.gcp.application.TodoItemDto;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ConfirmationPublisher {
    private static final String MESSAGE_CONTENT = "The new todo item \"%s\" is created. It's due date is set to %s.";
    private static final String DATE_FORMAT = "dd.MM.yyyy";
    private final PubSubTemplate pubSubTemplate;
    @Value("{pubsub.topic}")
    private String topic;

    public void publish(TodoItemDto todo) {
        this.pubSubTemplate.publish(this.topic, buildPubSubMessage(todo));
    }

    private PubsubMessage buildPubSubMessage(TodoItemDto todo) {
        return PubsubMessage.newBuilder()
                .setData(buildConfirmationMessage(todo))
                .setMessageId(buildMessageUUID())
                .build();
    }

    private ByteString buildConfirmationMessage(TodoItemDto todo) {
        DateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
        String formattedDate = formatter.format(todo.getDate());
        return ByteString.copyFromUtf8(buildMessageContent(todo, formattedDate));
    }

    private String buildMessageContent(TodoItemDto todo, String formattedDate) {
        return todo.getAddress() + ";" + todo.getTitle() + " event created;" +
                String.format(MESSAGE_CONTENT, todo.getTitle(), formattedDate);
    }

    private String buildMessageUUID() {
        return this.topic + "_" + UUID.randomUUID();
    }
}
