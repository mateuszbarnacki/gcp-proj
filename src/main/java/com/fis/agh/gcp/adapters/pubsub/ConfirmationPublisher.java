package com.fis.agh.gcp.adapters.pubsub;

import com.fis.agh.gcp.application.TodoItemDto;
import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutureCallback;
import com.google.api.core.ApiFutures;
import com.google.api.gax.rpc.ApiException;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;
import com.google.pubsub.v1.TopicName;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class ConfirmationPublisher {
    private static final String MESSAGE_CONTENT = "The new todo item \"%s\" is created. It's due date is set to %s.";
    private static final String DATE_FORMAT = "dd.MM.yyyy";
    private static final String PROJECT_ID = "primeval-legacy-375300";
    private static final String TOPIC_ID = "confirmation-topic";

    public void publish(TodoItemDto todo) throws IOException, InterruptedException {
        TopicName topicName = TopicName.of(PROJECT_ID, TOPIC_ID);
        Publisher publisher = Publisher.newBuilder(topicName)
                .setEndpoint("europe-west3-pubsub.googleapis.com:443")
                .setEnableMessageOrdering(true)
                .build();

        try {
            ApiFuture<String> future = publishTodoItem(publisher, todo);
            createFutureCallback(future);
        } finally {
            publisher.shutdown();
            publisher.awaitTermination(1, TimeUnit.MINUTES);
        }
    }

    private ApiFuture<String> publishTodoItem(Publisher publisher, TodoItemDto todo) {
        ByteString data = buildConfirmationMessage(todo);
        PubsubMessage pubsubMessage =
                PubsubMessage.newBuilder().setData(data).build();
        return publisher.publish(pubsubMessage);
    }

    private void createFutureCallback(ApiFuture<String> future) {
        ApiFutures.addCallback(
                future,
                new ApiFutureCallback<String>() {
                    @Override
                    public void onFailure(Throwable throwable) {
                        if (throwable instanceof ApiException) {
                            ApiException apiException = ((ApiException) throwable);
                            System.out.println(apiException.getStatusCode().getCode());
                            System.out.println(apiException.isRetryable());
                        }
                        System.out.println("Error publishing message");
                    }

                    @Override
                    public void onSuccess(String messageId) {
                        System.out.println("Success" + " : " + messageId);
                    }
                },
                MoreExecutors.directExecutor());
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

}

