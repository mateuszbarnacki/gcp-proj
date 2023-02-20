package com.fis.agh.gcp;

import com.fis.agh.gcp.pubsub.MessageCreator;
import com.fis.agh.gcp.service.TodoItemDto;
import com.google.protobuf.ByteString;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;

class MessageCreatorTest {
    private static final String DATE_FORMAT = "yy.MM.yyyy";
    private final MessageCreator messageCreator;

    MessageCreatorTest() {
        this.messageCreator = new MessageCreator(new SimpleDateFormat(DATE_FORMAT));
    }

    @Test
    void shouldCreateMessage() {
        // given
        TodoItemDto todoItem = buildTodoItem();

        // when
        ByteString message = messageCreator.create(todoItem);

        // then
        String text = message.toStringUtf8();
        Assertions.assertFalse(text.isBlank());
        Assertions.assertEquals("test@test.pl;" +
                "Test Message event created;" +
                "The new todo item \"Test Message\" is created. " +
                "It's due date is set to " + getFormatedDateAsString() + ".", text);
    }

    private TodoItemDto buildTodoItem() {
        return TodoItemDto.builder()
                .address("test@test.pl")
                .title("Test Message")
                .content("Lorem ipsum")
                .date(Date.from(Instant.now()))
                .build();
    }

    private String getFormatedDateAsString() {
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        return dateFormat.format(date);
    }
}
