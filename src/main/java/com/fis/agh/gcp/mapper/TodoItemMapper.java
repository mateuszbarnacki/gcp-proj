package com.fis.agh.gcp.mapper;

import com.fis.agh.gcp.dto.TodoItemDto;
import com.fis.agh.gcp.model.TodoItem;
import org.springframework.stereotype.Component;

@Component
public class TodoItemMapper {

    public TodoItemDto mapToDto(TodoItem item) {
        return TodoItemDto.builder()
                .title(item.getTitle())
                .content(item.getContent())
                //.dueDate(item.getDate())
                //.completed(item.isCompleted())
                .build();
    }

    public TodoItem mapToEntity(TodoItemDto dto) {
        TodoItem item = new TodoItem();
        item.setTitle(dto.getTitle());
        item.setContent(dto.getContent());
        //item.setDate(dto.getDueDate());
        //item.setCompleted(dto.isCompleted());
        return item;
    }

}
