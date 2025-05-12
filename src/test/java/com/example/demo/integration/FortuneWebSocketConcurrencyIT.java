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
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.concurrent.*;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class FortuneWebSocketConcurrencyIT {

    @LocalServerPort
    int port;

    @Autowired
    FortuneHistoryService historyService;

    private WebSocketStompClient stompClient;

    @BeforeEach
    void setup() {
        stompClient = new WebSocketStompClient(new StandardWebSocketClient());
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
    }

    @Test
    @DisplayName("100 クライアントに 1 件のメッセージが漏れなく届く")
    void broadcastToHundredClients() throws Exception {
        int clients = 100;
        CountDownLatch latch = new CountDownLatch(clients);

        for (int i = 0; i < clients; i++) {
            stompClient.connect("ws://localhost:" + port + "/ws/fortune",
                    new StompSessionHandlerAdapter() {
                        @Override
                        public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
                            session.subscribe("/topic/fortune", new StompFrameHandler() {
                                @Override public Type getPayloadType(StompHeaders h) { return FortuneHistory.class; }
                                @Override public void handleFrame(StompHeaders h, Object o) { latch.countDown(); }
                            });
                        }
                    });
        }

        Thread.sleep(500);

        historyService.saveHistory("Bulk", "misFortune");

        boolean allGot = latch.await(3, TimeUnit.SECONDS);
        assertThat(allGot).isTrue();
    }
}
