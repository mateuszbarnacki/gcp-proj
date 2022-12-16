package com.fis.agh.gcp.model;

import lombok.Data;
import org.springframework.cloud.gcp.data.datastore.core.mapping.Entity;
import org.springframework.data.annotation.Id;

import java.time.ZonedDateTime;

@Entity(name = "todo_items")
@Data
public class TodoItem {
    @Id
    private Long id;
    private String title;
    private String content;
    private ZonedDateTime date;
    private boolean completed;
}
