package com.example.demo.service;

import com.example.demo.model.FortuneHistory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * FortuneHistoryService のユニットテスト
 */
class FortuneHistoryServiceTest {

    private FortuneHistoryService historyService;

    @BeforeEach
    void setUp() {
        historyService = new FortuneHistoryService();
    }

    @Test
    @DisplayName("saveHistory で履歴が 1 件追加される")
    void saveHistory_addsEntry() {
        assertTrue(historyService.getAllHistory().isEmpty());

        historyService.saveHistory("Alice", "greatFortune");

        List<FortuneHistory> list = historyService.getAllHistory();
        assertEquals(1, list.size());

        FortuneHistory saved = list.get(0);
        assertEquals("Alice", saved.getUserName());
        assertEquals("greatFortune", saved.getFortune());
        assertNotNull(saved.getDateTime());
    }

    @Test
    @DisplayName("getAllHistory で返るリストは不変 (UnsupportedOperationException)")
    void getAllHistory_returnsUnmodifiableList() {
        historyService.saveHistory("Bob", "middleFortune");
        List<FortuneHistory> list = historyService.getAllHistory();

        assertThrows(UnsupportedOperationException.class,
                     () -> list.add(new FortuneHistory("X", "smallFortune", null)));
    }

    @Test
    @DisplayName("多スレッド環境でも saveHistory が安全に動作する")
    void saveHistory_threadSafety() throws InterruptedException {
        int threads = 10;
        int callsPerThread = 500;
        ExecutorService executor = Executors.newFixedThreadPool(threads);

        CountDownLatch latch = new CountDownLatch(threads);
        for (int t = 0; t < threads; t++) {
            executor.submit(() -> {
                for (int i = 0; i < callsPerThread; i++) {
                    historyService.saveHistory("User" + i, "smallFortune");
                }
                latch.countDown();
            });
        }

        latch.await(5, TimeUnit.SECONDS);
        executor.shutdownNow();

        assertEquals(threads * callsPerThread, historyService.getAllHistory().size());
    }
}
