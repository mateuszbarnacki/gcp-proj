package com.fis.agh.gcp.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.ZonedDateTime;

@Builder
@Getter
public class TodoItemDto {
    private final String title;
    private final String content;
    private final ZonedDateTime dueDate;
    private final boolean completed;
}
