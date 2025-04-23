package com.example.demo.service;

import com.example.demo.config.FortuneRateProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

/**
 * FortuneService のユニットテスト<br>
 * - getRandomFortune の分布と境界条件を検証<br>
 * - FortuneRateProperties は Mockito でモック化<br>
 */
@org.junit.jupiter.api.extension.ExtendWith(MockitoExtension.class)
class FortuneServiceTest {

    @Mock
    private FortuneRateProperties rateProperties;

    private FortuneService fortuneService;

    @BeforeEach
    void setUp() {
        openMocks(this);
        fortuneService = new FortuneService(rateProperties);

        // デフォルト確率 (合計1.0)
        given(rateProperties.getGreat()).willReturn(0.1);
        given(rateProperties.getMiddle()).willReturn(0.3);
        given(rateProperties.getSmall()).willReturn(0.4);
        given(rateProperties.getMis()).willReturn(0.2);
    }

    @Test
    @DisplayName("getRandomFortune を十分回呼ぶと全種類の運勢が出現する")
    void shouldReturnAllFortuneTypes() {
        Set<String> results = new HashSet<>();

        for (int i = 0; i < 1_000; i++) {
            results.add(fortuneService.getRandomFortune());
        }

        assertTrue(results.contains("greatFortune"));
        assertTrue(results.contains("middleFortune"));
        assertTrue(results.contains("smallFortune"));
        assertTrue(results.contains("misFortune"));
    }

    @Test
    @DisplayName("確率分布が設定通り(±5% 誤差以内)になる")
    void shouldMatchConfiguredDistributionWithinTolerance() {
        final int totalCalls = 100_000;
        Map<String, Integer> counts = new HashMap<>();
        counts.put("greatFortune", 0);
        counts.put("middleFortune", 0);
        counts.put("smallFortune", 0);
        counts.put("misFortune", 0);

        for (int i = 0; i < totalCalls; i++) {
            counts.computeIfPresent(fortuneService.getRandomFortune(), (k, v) -> v + 1);
        }

        double delta = 0.05; // 5% 許容誤差
        assertEquals(0.1, (double) counts.get("greatFortune") / totalCalls, delta);
        assertEquals(0.3, (double) counts.get("middleFortune") / totalCalls, delta);
        assertEquals(0.4, (double) counts.get("smallFortune")  / totalCalls, delta);
        assertEquals(0.2, (double) counts.get("misFortune")    / totalCalls, delta);
    }

    @Test
    @DisplayName("全確率が 0 の場合は undefinedFortune を返す")
    void shouldReturnUndefinedWhenRatesAreZero() {
        given(rateProperties.getGreat()).willReturn(0.0);
        given(rateProperties.getMiddle()).willReturn(0.0);
        given(rateProperties.getSmall()).willReturn(0.0);
        given(rateProperties.getMis()).willReturn(0.0);

        assertEquals("undefinedFortune", fortuneService.getRandomFortune());
    }

    @Test
    @DisplayName("great=1.0, 他=0 のとき常に greatFortune を返す")
    void shouldAlwaysReturnGreatWhenGreatRateIsOne() {
        given(rateProperties.getGreat()).willReturn(1.0);
        given(rateProperties.getMiddle()).willReturn(0.0);
        given(rateProperties.getSmall()).willReturn(0.0);
        given(rateProperties.getMis()).willReturn(0.0);

        for (int i = 0; i < 100; i++) {
            assertEquals("greatFortune", fortuneService.getRandomFortune());
        }
    }
}
