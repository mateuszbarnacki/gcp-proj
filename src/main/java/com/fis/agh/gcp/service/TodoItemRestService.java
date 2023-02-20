package com.fis.agh.gcp.service;

import com.fis.agh.gcp.exception.InvalidTodoDtoException;
import com.fis.agh.gcp.pubsub.ConfirmationPublisher;
import com.fis.agh.gcp.validator.ItemValidator;
import com.fis.agh.gcp.validator.ValidationResult;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;

@Service
@RequiredArgsConstructor
public class TodoItemRestService implements TodoItemService {
    private static final String PUBLISHED = "Published";
    private final Logger logger = LoggerFactory.getLogger(TodoItemRestService.class);
    private final ItemValidator itemValidator;
    private final ConfirmationPublisher confirmationPublisher;

    public String publishItem(@NotNull TodoItemDto todoItem) {
        ValidationResult validationResult = itemValidator.validateTodoItem(todoItem);
        if (!validationResult.validate()) {
            logValidationError(validationResult);
        }
        confirmationPublisher.publish(todoItem);
        return PUBLISHED;
    }

    private void logValidationError(ValidationResult validationResult) {
        logger.error(validationResult.getMessages());
        throw new InvalidTodoDtoException(validationResult.getMessages());
    }
}
