package com.example.demo.service;

import com.example.demo.model.FortuneHistory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class FortuneStatisticsServiceTest {

    private FortuneHistoryService historyService;
    private FortuneStatisticsService statisticsService;

    @BeforeEach
    void setUp() {
        historyService = new FortuneHistoryService();
        statisticsService = new FortuneStatisticsService(historyService);

        historyService.saveHistory("Alice", "greatFortune");
        historyService.saveHistory("Bob",   "smallFortune");
        historyService.saveHistory("Alice", "smallFortune");
        historyService.saveHistory("Alice", "greatFortune");
    }

    @Test
    @DisplayName("全体の出現回数が正しい")
    void totalCounts() {
        Map<String, Long> totals = statisticsService.getTotalFortuneCounts();
        assertEquals(2L, totals.get("greatFortune"));
        assertEquals(2L, totals.get("smallFortune"));
    }

    @Test
    @DisplayName("ユーザー別の出現回数が正しい")
    void userCounts() {
        Map<String, Long> alice = statisticsService.getUserFortuneCounts("Alice");
        assertEquals(2L, alice.get("greatFortune"));
        assertEquals(1L, alice.get("smallFortune"));

        Map<String, Long> bob = statisticsService.getUserFortuneCounts("Bob");
        assertEquals(1L, bob.get("smallFortune"));
    }

    @Test
    @DisplayName("最新履歴が取得できる")
    void latestHistory() {
        FortuneHistory latest = statisticsService.getLatestHistory().orElseThrow();
        assertEquals("Alice", latest.getUserName());
        assertEquals("greatFortune", latest.getFortune());
    }
}
