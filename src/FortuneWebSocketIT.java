package com.example.demo.integration;

import com.example.demo.model.FortuneHistory;
import com.example.demo.service.FortuneHistoryService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.concurrent.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class FortuneWebSocketIT {

    @LocalServerPort
    int port;

    @Autowired
    FortuneHistoryService historyService;

    private WebSocketStompClient stompClient;
    private BlockingQueue<FortuneHistory> blockingQueue;

    @BeforeEach
    void setUp() {
        stompClient = new WebSocketStompClient(new StandardWebSocketClient());
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        blockingQueue = new LinkedBlockingQueue<>(1);
    }

    @Test
    @DisplayName("saveHistory → /topic/fortune に届く")
    void receiveTopicMessage() throws Exception {
        StompSession session = stompClient
                .connect("ws://localhost:" + port + "/ws/fortune",
                         new WebSocketHttpHeaders(),
                         new StompSessionHandlerAdapter() {})
                .get(1, TimeUnit.SECONDS);

        session.subscribe("/topic/fortune", new StompFrameHandler() {
            @Override public Type getPayloadType(StompHeaders headers) {
                return FortuneHistory.class;
            }
            @Override public void handleFrame(StompHeaders headers, Object payload) {
                blockingQueue.offer((FortuneHistory) payload);
            }
        });

        historyService.saveHistory("Eve", "greatFortune");

        FortuneHistory received =
                blockingQueue.poll(2, TimeUnit.SECONDS);

        assertThat(received)
                .isNotNull()
                .extracting(FortuneHistory::getUserName,
                            FortuneHistory::getFortune)
                .containsExactly("Eve", "greatFortune");
    }
}
