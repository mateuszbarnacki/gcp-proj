package com.fis.agh.gcp.validator;

import com.fis.agh.gcp.service.TodoItemDto;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class ItemValidator {
    private final List<Validating> validators;

    public ItemValidator() {
        this.validators = List.of(new TitleValidator(),
                new EmailAddressValidator(),
                new DateValidator());
    }

    public ValidationResult validateTodoItem(TodoItemDto todoItem) {
        return new ValidationResult(applyValidators(todoItem));
    }

    private List<String> applyValidators(TodoItemDto todoItem) {
        return this.validators.stream()
                .map(validating -> validating.validate(todoItem))
                .filter(Strings::isNotBlank)
                .collect(Collectors.toList());
    }
}

interface Validating {
    String validate(TodoItemDto dto);
}

class TitleValidator implements Validating {
    @Override
    public String validate(TodoItemDto dto) {
        String title = dto.getTitle();
        return validateTitle(title);
    }

    private String validateTitle(String title) {
        if (Objects.isNull(title) || title.isBlank()) {
            return "Title could not be blank!";
        }
        return Strings.EMPTY;
    }
}

class EmailAddressValidator implements Validating {
    @Override
    public String validate(TodoItemDto dto) {
        String emailAddress = dto.getAddress();
        return validateEmailAddress(emailAddress);
    }

    private String validateEmailAddress(String emailAddress) {
        if (!validateEmail(emailAddress)) {
            return "Invalid email address structure!";
        }
        return Strings.EMPTY;
    }

    private boolean validateEmail(String emailAddress) {
        String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        return Pattern.compile(regexPattern)
                .matcher(emailAddress)
                .matches();
    }
}

class DateValidator implements Validating {
    @Override
    public String validate(TodoItemDto dto) {
        Date date = dto.getDate();
        return validateDate(date);
    }

    private String validateDate(Date date) {
        if (date.compareTo(Date.from(Instant.now())) < 0) {
            return "Given date is in the past!";
        }
        return Strings.EMPTY;
    }
}
