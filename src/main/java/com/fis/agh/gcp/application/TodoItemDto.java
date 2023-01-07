package com.fis.agh.gcp.application;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.beans.ConstructorProperties;
import java.util.Date;

@Builder
@Getter
public class TodoItemDto {
    private final String address;
    private final String title;
    private final String content;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private final Date date;
    private final boolean completed;

    @ConstructorProperties({"address", "title", "content", "date", "completed"})
    public TodoItemDto(String address, String title, String content, Date date, boolean completed) {
        this.address = address;
        this.title = title;
        this.content = content;
        this.date = date;
        this.completed = completed;
    }
}
