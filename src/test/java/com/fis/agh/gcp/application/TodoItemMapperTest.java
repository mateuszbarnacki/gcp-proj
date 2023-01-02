package com.fis.agh.gcp.application;

import com.fis.agh.gcp.domain.TodoItem;
import com.google.cloud.Timestamp;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@SpringBootTest
class TodoItemMapperTest {

    @Autowired
    private TodoItemMapper mapper;

    @Test
    void shouldMapDtoToEntity() {
        TodoItemDto dto = givenTodoItemDto();

        TodoItem entity = mapper.mapToEntity(dto);

        AssertionsForClassTypes.assertThat(entity)
                .hasFieldOrPropertyWithValue("title", "Test1")
                .hasFieldOrPropertyWithValue("content", "Dummy content")
                .hasFieldOrPropertyWithValue("emailAddress", "test@test.pl")
                .hasFieldOrPropertyWithValue("completed", false);
    }

    @Test
    void shouldMapEntityToDto() {
        Date date = new GregorianCalendar(2023, Calendar.JANUARY, 13).getTime();
        TodoItem entity = givenTodoItemEntity();

        TodoItemDto dto = mapper.mapToDto(entity);

        AssertionsForClassTypes.assertThat(dto)
                .hasFieldOrPropertyWithValue("title", "Test2")
                .hasFieldOrPropertyWithValue("content", "Empty")
                .hasFieldOrPropertyWithValue("address", "test2@o2.pl")
                .hasFieldOrPropertyWithValue("date", date)
                .hasFieldOrPropertyWithValue("completed", false);
    }

    @Test
    void shouldMapDate() {
        Date date = new GregorianCalendar(2023, Calendar.APRIL, 1).getTime();

        Timestamp timestamp = mapper.mapDateToGoogleTimestamp(date);

        Assertions.assertEquals(0, timestamp.compareTo(Timestamp.of(date)));
    }

    private TodoItem givenTodoItemEntity() {

        TodoItem item = new TodoItem();

        item.setId(1L);
        item.setTitle("Test2");
        item.setContent("Empty");
        item.setEmailAddress("test2@o2.pl");
        item.setDate(Timestamp.of(new GregorianCalendar(2023, Calendar.JANUARY, 13).getTime()));
        item.setCompleted(false);

        return item;
    }

    private TodoItemDto givenTodoItemDto() {
        return TodoItemDto.builder()
                .title("Test1")
                .content("Dummy content")
                .address("test@test.pl")
                .completed(false)
                .date(new GregorianCalendar(2023, Calendar.JANUARY, 13).getTime())
                .build();
    }
}
