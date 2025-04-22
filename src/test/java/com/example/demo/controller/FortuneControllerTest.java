package com.example.demo.controller;

import com.example.demo.service.FortuneService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * /fortune 系 HTML ビューを返す Controller をテスト。
 * ViewResolver までは起動せず、論理ビュー名の検証を行う。
 */
@WebMvcTest(FortuneController.class)
class FortuneControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FortuneService fortuneService;

    @Test
    @DisplayName("GET /fortune でランダム運勢ビューが返る")
    void showRandomFortunePage_returnsExpectedView() throws Exception {
        given(fortuneService.getRandomFortune()).willReturn("middleFortune");

        mockMvc.perform(get("/fortune").param("name", "Carol"))
               .andExpect(status().isOk())
               .andExpect(view().name("middleFortune.html"))
               .andExpect(model().attribute("name", "Carol"));

        then(fortuneService).should().getRandomFortune();
    }

    @Test
    @DisplayName("GET /fortune/today で今日の運勢ビューが返る")
    void showTodayFortunePage_returnsExpectedView() throws Exception {
        given(fortuneService.getTodayFortune("Dave")).willReturn("misFortune");

        mockMvc.perform(get("/fortune/today").param("name", "Dave"))
               .andExpect(status().isOk())
               .andExpect(view().name("misFortune.html"))
               .andExpect(model().attribute("name", "Dave"));

        then(fortuneService).should().getTodayFortune("Dave");
    }
}
