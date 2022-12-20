package com.fis.agh.gcp.model;

import com.google.cloud.Timestamp;
import lombok.Data;
import org.springframework.cloud.gcp.data.datastore.core.mapping.Entity;
import org.springframework.cloud.gcp.data.datastore.core.mapping.Field;
import org.springframework.cloud.gcp.data.datastore.core.mapping.Unindexed;
import org.springframework.data.annotation.Id;

@Entity(name = "todo_items")
@Data
public class TodoItem {
    @Id
    private Long id;
    @Field(name = "email_address")
    private String address;
    private String title;
    @Unindexed
    private String content;
    private Timestamp date;
    @Unindexed
    private boolean completed;
}
