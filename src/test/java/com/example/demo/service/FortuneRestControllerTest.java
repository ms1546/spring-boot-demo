package com.example.demo.controller;

import com.example.demo.model.FortuneResponse;
import com.example.demo.service.FortuneService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * /api/fortune 系 REST API をテストする。
 * Controller 層のみを起動し、FortuneService はモック化。
 */
@WebMvcTest(FortuneRestController.class)
class FortuneRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FortuneService fortuneService;

    @Test
    @DisplayName("GET /api/fortune でランダム運勢が JSON 形式で返る")
    void getRandomFortune_returnsExpectedJson() throws Exception {
        given(fortuneService.getRandomFortune()).willReturn("greatFortune");

        mockMvc.perform(get("/api/fortune").param("name", "Alice"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.name",   is("Alice")))
               .andExpect(jsonPath("$.result", is("greatFortune")));

        then(fortuneService).should().getRandomFortune();
    }

    @Test
    @DisplayName("GET /api/fortune/today で今日の運勢が JSON 形式で返る")
    void getTodayFortune_returnsExpectedJson() throws Exception {
        given(fortuneService.getTodayFortune("Bob")).willReturn("smallFortune");

        mockMvc.perform(get("/api/fortune/today").param("name", "Bob"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.name",   is("Bob")))
               .andExpect(jsonPath("$.result", is("smallFortune")));

        then(fortuneService).should().getTodayFortune("Bob");
    }

    @Test
    @DisplayName("POST /api/fortune/clear でキャッシュクリアメッセージが返る")
    void clearCache_returnsOkMessage() throws Exception {
        willDoNothing().given(fortuneService).clearCache();

        mockMvc.perform(post("/api/fortune/clear"))
               .andExpect(status().isOk())
               .andExpect(content().string("Cleared all fortune cache."));

        then(fortuneService).should().clearCache();
    }
}
