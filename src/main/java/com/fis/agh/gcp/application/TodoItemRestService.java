package com.fis.agh.gcp.application;

import com.fis.agh.gcp.domain.InvalidTodoDtoException;
import com.fis.agh.gcp.adapters.pubsub.ConfirmationPublisher;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;

@Service
@RequiredArgsConstructor
public class TodoItemRestService implements TodoItemService {
    private static final String CREATED = "Created";
    private final Logger logger = LoggerFactory.getLogger(TodoItemRestService.class);
    private final ItemValidator validator;
    private final ConfirmationPublisher publisher;

    public String saveItem(@NotNull TodoItemDto todoItem) {
        ValidationResult validationResult = validator.validateTodoItem(todoItem);
        if (validationResult.validate()) {
            logger.error(validationResult.getMessages());
            throw new InvalidTodoDtoException(validationResult.getMessages());
        }

        try {
            publisher.publish(todoItem);
        } catch (Exception e) {
            logger.error("Could not publish todoItem: " + e.getMessage());
        }

        return CREATED;
    }
}
