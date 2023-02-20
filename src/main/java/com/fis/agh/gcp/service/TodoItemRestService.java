package com.fis.agh.gcp.service;

import com.fis.agh.gcp.exception.InvalidTodoDtoException;
import com.fis.agh.gcp.pubsub.ConfirmationPublisher;
import com.fis.agh.gcp.pubsub.MessageCreator;
import com.fis.agh.gcp.validator.ItemValidator;
import com.fis.agh.gcp.validator.ValidationResult;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.TopicName;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.text.SimpleDateFormat;

@Service
@RequiredArgsConstructor
public class TodoItemRestService implements TodoItemService {
    private static final String DATE_FORMAT = "dd.MM.yyyy";
    private static final Logger LOGGER = LoggerFactory.getLogger(TodoItemRestService.class);
    private final ItemValidator itemValidator;
    private final ConfirmationPublisher confirmationPublisher;
    @Value("{spring.cloud.gcp.project-id}")
    private String projectId;
    @Value("{gcp.topic-id}")
    private String topicId;

    @Override
    public void publishItem(@NotNull TodoItemDto todoItem) {
        ValidationResult validationResult = itemValidator.validateTodoItem(todoItem);
        if (!validationResult.validate()) {
            logValidationError(validationResult);
        }
        publish(todoItem);
    }

    private void publish(TodoItemDto todoItem) {
        ByteString message = createMessage(todoItem);
        TopicName topicName = TopicName.of(projectId, topicId);
        confirmationPublisher.publish(topicName, message);
    }

    private ByteString createMessage(TodoItemDto todoItem) {
        MessageCreator messageCreator = new MessageCreator(new SimpleDateFormat(DATE_FORMAT));
        return messageCreator.create(todoItem);
    }

    private void logValidationError(ValidationResult validationResult) {
        LOGGER.error(validationResult.getMessages());
        throw new InvalidTodoDtoException(validationResult.getMessages());
    }
}
