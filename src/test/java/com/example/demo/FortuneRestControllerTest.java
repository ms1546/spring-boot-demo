package com.example.demo.controller;

import com.example.demo.model.FortuneResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FortuneRestController.class)
class FortuneRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private com.example.demo.service.FortuneService fortuneService;

    @Test
    void testGetRandomFortuneAsJson() throws Exception {
        given(fortuneService.getRandomFortune()).willReturn("good");

        mockMvc.perform(get("/api/fortune")
                        .param("name", "User"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("User"))
                .andExpect(jsonPath("$.result").value("good"));
    }
}
