package com.fis.agh.gcp.application;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Calendar;
import java.util.GregorianCalendar;

@SpringBootTest
class ItemValidatorTest {
    @Autowired
    private ItemValidator validator;

    @Test
    void shouldBeValidTodoItemDto() {
        TodoItemDto item = givenValidTodoItemDto();

        ValidationResult validationResult = validator.validateTodoItem(item);
        String errorMessage = validationResult.getMessages();

        Assertions.assertTrue(validationResult.validate());
        Assertions.assertTrue(errorMessage.isBlank());
    }

    @Test
    void shouldBeValidQueryItemDto() {
        QueryItemDto queryItem = givenValidQueryItemDto();

        ValidationResult validationResult = validator.validateQueryItem(queryItem);
        String errorMessage = validationResult.getMessages();

        Assertions.assertTrue(validationResult.validate());
        Assertions.assertTrue(errorMessage.isBlank());
    }

    @Test
    void shouldReturnInvalidTitle() {
        TodoItemDto item = givenTodoItemDtoWithMissingTitle();

        ValidationResult validationResult = validator.validateTodoItem(item);
        String errorMessage = validationResult.getMessages();

        Assertions.assertFalse(validationResult.validate());
        Assertions.assertFalse(errorMessage.isBlank());
    }

    @Test
    void shouldReturnInvalidDate() {
        TodoItemDto item = givenTodoItemDtoWithDateInThePast();

        ValidationResult validationResult = validator.validateTodoItem(item);

        Assertions.assertFalse(validationResult.validate());
    }

    @Test
    void shouldReturnInvalidEmail() {
        QueryItemDto queryItem = givenQueryItemDtoWithInvalidEmailAddress();

        ValidationResult validationResult = validator.validateQueryItem(queryItem);
        String errorMessage = validationResult.getMessages();

        Assertions.assertFalse(errorMessage.isBlank());
    }

    @Test
    void shouldDetectMoreInvalidFields() {
        TodoItemDto item = givenInvalidTodoItemDto();

        ValidationResult validationResult = validator.validateTodoItem(item);

        Assertions.assertFalse(validationResult.validate());
    }

    private TodoItemDto givenValidTodoItemDto() {
        return TodoItemDto.builder()
                .address("test@test.com")
                .title("Title")
                .date(new GregorianCalendar(2033, Calendar.JANUARY, 13).getTime())
                .build();
    }

    private QueryItemDto givenValidQueryItemDto() {
        return QueryItemDto.builder()
                .emailAddress("test_1@o2.com")
                .date(new GregorianCalendar(2033, Calendar.MAY, 25).getTime())
                .build();
    }

    private TodoItemDto givenTodoItemDtoWithMissingTitle() {
        return TodoItemDto.builder()
                .address("test@test.com")
                .date(new GregorianCalendar(2033, Calendar.JANUARY, 13).getTime())
                .build();
    }

    private TodoItemDto givenTodoItemDtoWithDateInThePast() {
        return TodoItemDto.builder()
                .address("test@test.com")
                .date(new GregorianCalendar(2013, Calendar.JANUARY, 13).getTime())
                .build();
    }

    private QueryItemDto givenQueryItemDtoWithInvalidEmailAddress() {
        return QueryItemDto.builder()
                .emailAddress("test_1.@o2.com")
                .date(new GregorianCalendar(2033, Calendar.MAY, 25).getTime())
                .build();
    }

    private TodoItemDto givenInvalidTodoItemDto() {
        return TodoItemDto.builder()
                .address("test@te.st.com")
                .date(new GregorianCalendar(2033, Calendar.JANUARY, 13).getTime())
                .build();
    }
}
