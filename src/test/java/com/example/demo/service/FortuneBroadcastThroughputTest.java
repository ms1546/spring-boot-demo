package com.example.demo.service;

import com.example.demo.model.FortuneHistory;
import org.junit.jupiter.api.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.time.LocalDateTime;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FortuneBroadcastThroughputTest {

    private FortuneBroadcastService broadcastService;
    private AtomicInteger stompCounter;

    @BeforeEach
    void init() {
        SimpMessagingTemplate template = mock(SimpMessagingTemplate.class);
        stompCounter = new AtomicInteger();
        doAnswer(inv -> { stompCounter.incrementAndGet(); return null; })
                .when(template).convertAndSend(eq("/topic/fortune"), any());

        broadcastService = new FortuneBroadcastService(template);
    }

    @Test
    @DisplayName("高頻度 broadcast (1 万件) を 1 秒内で安全処理")
    void highThroughput() throws InterruptedException {
        int total = 10_000;
        ExecutorService pool = Executors.newFixedThreadPool(8);

        CountDownLatch latch = new CountDownLatch(total);
        for (int i = 0; i < total; i++) {
            pool.submit(() -> {
                broadcastService.broadcast(
                        new FortuneHistory("U", "smallFortune", LocalDateTime.now()));
                latch.countDown();
            });
        }
        boolean finished = latch.await(2, TimeUnit.SECONDS);
        pool.shutdownNow();

        assertThat(finished).isTrue();
        assertThat(stompCounter.get()).isEqualTo(total);
    }
}
