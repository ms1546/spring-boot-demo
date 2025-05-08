package com.example.demo.controller;

import com.example.demo.service.FortuneBroadcastService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import static org.hamcrest.Matchers.startsWith;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(FortuneSseController.class)
class FortuneSseControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    FortuneBroadcastService broadcastService;

    @Test
    @DisplayName("GET /api/fortune/stream が SSE Content-Type を返す")
    void connectStream() throws Exception {
        given(broadcastService.subscribeSse()).willReturn(new SseEmitter(0L));

        mvc.perform(get("/api/fortune/stream"))
           .andExpect(status().isOk())
           .andExpect(header().string("Content-Type",
                  startsWith(MediaType.TEXT_EVENT_STREAM_VALUE)));
    }
}
