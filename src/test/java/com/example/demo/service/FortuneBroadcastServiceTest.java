package com.example.demo.service;

import com.example.demo.model.FortuneHistory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class FortuneBroadcastServiceTest {

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private FortuneBroadcastService broadcastService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("broadcast で SSE emitter にイベントが送信される")
    void sendSse() throws Exception {
        SseEmitter emitter = broadcastService.subscribeSse();

        FortuneHistory history =
                new FortuneHistory("Alice", "greatFortune", LocalDateTime.now());

        broadcastService.broadcast(history);

        List<SseEmitter> list = TestUtils.getField(broadcastService, "emitters");
        assert list.contains(emitter);
    }

    @Test
    @DisplayName("broadcast で /topic/fortune に STOMP 送信される")
    void sendWebSocket() {
        FortuneHistory history =
                new FortuneHistory("Bob", "smallFortune", LocalDateTime.now());

        broadcastService.broadcast(history);

        verify(messagingTemplate, times(1))
                .convertAndSend(eq("/topic/fortune"), eq(history));
    }
}
