package com.fis.agh.gcp.application;

import com.fis.agh.gcp.domain.TodoItem;
import com.fis.agh.gcp.domain.TodoItemRepository;
import com.google.cloud.Timestamp;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
class TodoItemRestServiceTest {
    private static final String EMAIL_ADDRESS = "test@test.pl";
    private static final Date DATE = new GregorianCalendar(2023, Calendar.JANUARY, 13).getTime();
    @Mock
    private TodoItemRepository repository;
    @Mock
    private TodoItemMapper mapper;
    @Mock
    private ItemValidator validator;
    @InjectMocks
    private TodoItemRestService service;

    @Test
    void whenNullShouldThrowNullPointerException() {
        Exception e = Assertions.assertThrows(NullPointerException.class, () -> service.queryTodoItems(null));
        Assertions.assertNotNull(e);
    }

    @Test
    void shouldReturnUserItem() {
        QueryItemDto givenQueryItem = buildQueryItem();
        Mockito.when(validator.validateQueryItem(any(QueryItemDto.class))).thenReturn(new ValidationResult(List.of()));
        Mockito.when(repository.getUserTodoItems(EMAIL_ADDRESS, false)).thenReturn(List.of(buildTodoItem()));
        Mockito.when(mapper.mapToDto(buildTodoItem())).thenReturn(buildTodoItemDto());

        List<TodoItemDto> items = service.getUserTodoItems(givenQueryItem);

        Assertions.assertFalse(items.isEmpty());
        Mockito.verify(repository).getUserTodoItems(anyString(), anyBoolean());
    }

    @Test
    void shouldReturnListOfTodoItemsWhichShouldBeDone() {
        QueryItemDto givenQueryItemWithDate = buildQueryItemWithDate();
        Mockito.when(validator.validateQueryItem(any(QueryItemDto.class))).thenReturn(new ValidationResult(List.of()));
        Mockito.when(mapper.mapDateToGoogleTimestamp(DATE)).thenReturn(Timestamp.of(DATE));
        Mockito.when(repository.getTodoItemsToBeDone(EMAIL_ADDRESS, Timestamp.of(DATE)))
                .thenReturn(List.of(buildTodoItem()));
        Mockito.when(mapper.mapToDto(buildTodoItem())).thenReturn(buildTodoItemDto());

        service.getItemsWhichShouldBeDone(givenQueryItemWithDate);

        Mockito.verify(repository).getTodoItemsToBeDone(anyString(), any(Timestamp.class));
    }

    @Test
    void shouldDeleteAllUserTodoItems() {
        QueryItemDto givenQueryItem = buildQueryItem();
        Mockito.when(validator.validateQueryItem(any(QueryItemDto.class))).thenReturn(new ValidationResult(List.of()));
        Mockito.when(repository.getAllByEmailAddress(EMAIL_ADDRESS)).thenReturn(List.of(buildTodoItem()));

        service.deleteAllUserTodoItems(givenQueryItem);

        Mockito.verify(repository).deleteAll(anyList());
    }

    private TodoItem buildTodoItem() {
        TodoItem item = new TodoItem();

        item.setId(1L);
        item.setTitle("Test");
        item.setContent("Dummy content");
        item.setEmailAddress(EMAIL_ADDRESS);
        item.setDate(Timestamp.of(DATE));
        item.setCompleted(false);

        return item;
    }

    private TodoItemDto buildTodoItemDto() {
        return TodoItemDto.builder()
                .title("Test")
                .content("Dummy content")
                .address(EMAIL_ADDRESS)
                .completed(false)
                .date(DATE)
                .build();
    }

    private QueryItemDto buildQueryItem() {
        return QueryItemDto.builder()
                .emailAddress(EMAIL_ADDRESS)
                .completed(false)
                .build();
    }

    private QueryItemDto buildQueryItemWithDate() {
        return QueryItemDto.builder()
                .emailAddress(EMAIL_ADDRESS)
                .date(DATE)
                .build();
    }
}
