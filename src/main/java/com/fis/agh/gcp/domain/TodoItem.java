package com.fis.agh.gcp.domain;

import com.google.cloud.Timestamp;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class TodoItem {
    @Id
    private String emailAddress;
    private String title;
    private String content;
    private Timestamp date;
    private boolean completed;
}
