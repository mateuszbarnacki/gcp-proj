package com.fis.agh.gcp.application;

import com.fis.agh.gcp.domain.TodoItem;
import com.google.cloud.Timestamp;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TodoItemMapper {

    public TodoItemDto mapToDto(TodoItem item) {
        Timestamp timestamp = item.getDate();
        return TodoItemDto.builder()
                .address(item.getAddress())
                .title(item.getTitle())
                .content(item.getContent())
                .date(timestamp.toDate())
                .completed(item.isCompleted())
                .build();
    }

    public TodoItem mapToEntity(TodoItemDto dto) {
        TodoItem item = new TodoItem();
        Timestamp timestamp = mapDateToGoogleTimestamp(dto.getDate());

        item.setAddress(dto.getAddress());
        item.setTitle(dto.getTitle());
        item.setContent(dto.getContent());
        item.setDate(timestamp);
        item.setCompleted(dto.isCompleted());

        return item;
    }

    public Timestamp mapDateToGoogleTimestamp(Date date) {
        return Timestamp.of(date);
    }
}
