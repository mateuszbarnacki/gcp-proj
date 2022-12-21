package com.fis.agh.gcp.application;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.beans.ConstructorProperties;
import java.util.Date;

@Builder
@Getter
public class QueryItemDto {
    private final String emailAddress;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private final Date date;
    private final boolean completed;

    @ConstructorProperties({"emailAddress", "date", "completed"})
    public QueryItemDto(String emailAddress, Date date, boolean completed) {
        this.emailAddress = emailAddress;
        this.date = date;
        this.completed = completed;
    }
}
