package com.example.demo.service;

import com.example.demo.model.FortuneHistory;
import org.junit.jupiter.api.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.assertj.core.api.Assertions.assertThat;

class FortuneBroadcastServiceSseCleanupTest {

    private FortuneBroadcastService broadcastService;

    @BeforeEach
    void setUp() {
        broadcastService =
                new FortuneBroadcastService(msg -> {/* STOMP 無効化 */});
    }

    @Test
    @DisplayName("SSE emitter タイムアウト後にリークせず削除される")
    void emitterRemovedAfterTimeout() throws Exception {
        SseEmitter emitter = new SseEmitter(100L);

        List<SseEmitter> list = new CopyOnWriteArrayList<>();
        list.add(emitter);
        TestUtils.setField(broadcastService, "emitters", list);

        Thread.sleep(200);
        broadcastService.broadcast(
                new FortuneHistory("A", "greatFortune", LocalDateTime.now()));

        assertThat(list).isEmpty();
    }
}
