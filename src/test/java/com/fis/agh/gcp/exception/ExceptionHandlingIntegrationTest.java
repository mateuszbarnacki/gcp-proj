package com.fis.agh.gcp.exception;

import com.fis.agh.gcp.exception.handler.RestApiErrorHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@AutoConfigureMockMvc(addFilters = false)
class ExceptionHandlingIntegrationTest {
    private MockMvc mvc;

    @BeforeEach
    public void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(new DummyErrorController())
                .setControllerAdvice(new RestApiErrorHandler())
                .build();
    }

    @Test
    void shouldReturnBadRequestCausedByInvalidTodoDateException() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/error/invalid-date"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Invalid date!"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("BAD_REQUEST"));
    }

    @Test
    void shouldReturnBadRequestCausedByInvalidTodoDtoException() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/error/invalid-dto"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Invalid dto!"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("BAD_REQUEST"));
    }

    @Test
    void shouldReturnNotFoundCausedByItemNotFoundException() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/error/item-not-found"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Not found!"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("NOT_FOUND"));
    }

    @Test
    void shouldReturnInternalServerErrorCausedByItemNotSavedException() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/error/item-not-saved"))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Not saved!"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("INTERNAL_SERVER_ERROR"));
    }
}
