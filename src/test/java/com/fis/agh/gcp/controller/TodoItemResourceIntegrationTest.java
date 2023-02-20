package com.fis.agh.gcp.controller;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fis.agh.gcp.service.TodoItemDto;
import com.fis.agh.gcp.service.TodoItemRestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc(addFilters = false)
class TodoItemResourceIntegrationTest {
    private MockMvc mvc;
    @MockBean
    private TodoItemRestService service;

    @BeforeEach
    public void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(new TodoItemResource(service)).build();
    }

    @Test
    void shouldPublishTodoItem() throws Exception {
        Object jsonObject = new Object() {
            private final String emailAddress = "test4@gmail.pl";
            private final Date date = new GregorianCalendar(2023, Calendar.MARCH, 11).getTime();
        };

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        String json = objectMapper.writeValueAsString(jsonObject);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/item")
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(json);

        mvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isCreated());

        Mockito.verify(service).publishItem(any(TodoItemDto.class));
    }
}
