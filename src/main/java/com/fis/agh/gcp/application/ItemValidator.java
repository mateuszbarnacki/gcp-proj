package com.fis.agh.gcp.application;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

@Component
public class ItemValidator {
    private final List<String> validationMessages;

    public ItemValidator() {
        this.validationMessages = new ArrayList<>();
    }

    public ValidationResult validateTodoItem(TodoItemDto todoItem) {
        this.validationMessages.clear();
        validateTitle(todoItem.getTitle());
        validateEmailAddress(todoItem.getAddress());
        validateDate(todoItem.getDate());
        return new ValidationResult(this.validationMessages);
    }

    public ValidationResult validateQueryItem(QueryItemDto queryItem) {
        this.validationMessages.clear();
        validateEmailAddress(queryItem.getEmailAddress());
        validateDate(queryItem.getDate());
        return new ValidationResult(this.validationMessages);
    }

    private void validateTitle(String title) {
        if (Objects.isNull(title) || title.isBlank()) {
            this.validationMessages.add("Title could not be blank!");
        }
    }

    private void validateEmailAddress(String emailAddress) {
        if (!validateEmail(emailAddress)) {
            this.validationMessages.add("Invalid email address structure!");
        }
    }

    private void validateDate(Date date) {
        if (date.compareTo(Date.from(Instant.now())) < 0) {
            this.validationMessages.add("Given date is in the past!");
        }
    }

    private boolean validateEmail(String emailAddress) {
        String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        return Pattern.compile(regexPattern)
                .matcher(emailAddress)
                .matches();
    }
}
