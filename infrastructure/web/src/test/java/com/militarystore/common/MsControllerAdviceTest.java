package com.militarystore.common;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MsTestController.class)
@ContextConfiguration(classes = {MsTestController.class, MsControllerAdvice.class})
class MsControllerAdviceTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void handleMilitaryStoreValidationException() throws Exception {
        mockMvc.perform(get("/test"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error").value("error message"))
            .andExpect(jsonPath("$.reason").value("MsValidationException: error message"));
    }

    @Test
    void handleMilitaryStoryNotFoundException() throws Exception {
        mockMvc.perform(get("/test/404"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.error").value("not found"))
            .andExpect(jsonPath("$.reason").value("MsNotFoundException: not found"));
    }

    @Test
    void handleRuntimeException() throws Exception {
        mockMvc.perform(get("/test/500"))
            .andExpect(status().is5xxServerError())
            .andExpect(jsonPath("$.error").value("Internal Server Error"))
            .andExpect(jsonPath("$.reason").value("ServiceUnavailableException: Service Unavailable"));
    }
}