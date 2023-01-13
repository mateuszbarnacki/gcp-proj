package com.fis.agh.gcp.infrastructure.controller;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fis.agh.gcp.application.QueryItemDto;
import com.fis.agh.gcp.application.TodoItemDto;
import com.fis.agh.gcp.application.TodoItemRestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@SpringBootTest(classes = TodoItemResourceImpl.class)
@AutoConfigureMockMvc(addFilters = false)
class TodoItemResourceIntegrationTest {
    private MockMvc mvc;
    @MockBean
    private TodoItemRestService service;

    @BeforeEach
    public void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(new TodoItemResourceImpl(service)).build();
    }

    @Test
    void shouldReturnUserTodoItems() throws Exception {
        String user = "test";
        Mockito.when(service.getUserTodoItems(anyString()))
                .thenReturn(List.of(TodoItemDto.builder().address("test@test.pl").title("Test").content("Content").build()));

        mvc.perform(MockMvcRequestBuilders.get("/item/{user}", user))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].address").value("test@test.pl"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("Test"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].content").value("Content"));
    }

    @Test
    void shouldCreateTodoItem() throws Exception {
        Object jsonObject = new Object() {
            private final String emailAddress = "test4@gmail.pl";
            private final Date date = new GregorianCalendar(2023, Calendar.MARCH, 11).getTime();
            private final boolean completed = false;
        };

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        String json = objectMapper.writeValueAsString(jsonObject);

        Mockito.when(service.saveItem(any(TodoItemDto.class)))
                .thenReturn(TodoItemDto.builder().address("test4@gmail.pl").title("Test4").build());

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/item")
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(json);

        mvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.address").value("test4@gmail.pl"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Test4"));

        Mockito.verify(service).saveItem(any(TodoItemDto.class));
    }


    @Test
    void shouldDeleteAllUsersTodoItems() throws Exception {
        Object jsonObject = new Object() {
            private final String emailAddress = "test4@gmail.pl";
            private final Date date = new GregorianCalendar(2023, Calendar.MARCH, 11).getTime();
            private final boolean completed = false;
        };

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        String json = objectMapper.writeValueAsString(jsonObject);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/item/all")
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(json);

        mvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(service).deleteAllUserTodoItems(any(QueryItemDto.class));
    }

    @Test
    void shouldDeleteDoneTodoItems() throws Exception {
        Object jsonObject = new Object() {
            private final String emailAddress = "test4@gmail.pl";
            private final Date date = new GregorianCalendar(2023, Calendar.MARCH, 11).getTime();
            private final boolean completed = false;
        };

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        String json = objectMapper.writeValueAsString(jsonObject);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/item/done")
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(json);

        mvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(service).deleteDoneTodoItems(any(QueryItemDto.class));
    }
}
