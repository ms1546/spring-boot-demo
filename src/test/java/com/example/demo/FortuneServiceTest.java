package com.example.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

/**
 * FortuneService の動作をテストするクラス
 * - ランダムな運勢が想定どおりの確率で出ているかを検証
 * - 全種類の運勢が出るかを検証
 * - エッジケース(確率が合計0の場合など)の振る舞いを検証
 */
class FortuneServiceTest {

    private FortuneService fortuneService;

    @BeforeEach
    void setUp() {

        fortuneService = new FortuneService();

        fortuneService.setGreat(0.1);
        fortuneService.setMiddle(0.3);
        fortuneService.setSmall(0.4);
        fortuneService.setMis(0.2);
    }

    @Test
    @DisplayName("複数回呼び出した際に、全ての運勢タイプが最低1回は出現すること")
    void shouldReturnAllTypesWithinMultipleRandomCalls() {
        Set<String> results = new HashSet<>();

        for (int i = 0; i < 1000; i++) {
            results.add(fortuneService.getRandomFortune());
        }

        assertTrue(results.contains("greatFortune"),
            "Expected the set to contain 'greatFortune' but it did not.");
        assertTrue(results.contains("middleFortune"),
            "Expected the set to contain 'middleFortune' but it did not.");
        assertTrue(results.contains("smallFortune"),
            "Expected the set to contain 'smallFortune' but it did not.");
        assertTrue(results.contains("misFortune"),
            "Expected the set to contain 'misFortune' but it did not.");
    }

    @Test
    @DisplayName("多数回呼び出した際に、設定した確率分布の範囲内に収まること(±5%誤差許容)")
    void shouldApproximateDistributionWithinAcceptedErrorMargin() {
        final int totalCalls = 100_000;

        Map<String, Integer> resultCountMap = new HashMap<>();
        resultCountMap.put("greatFortune",  0);
        resultCountMap.put("middleFortune", 0);
        resultCountMap.put("smallFortune",  0);
        resultCountMap.put("misFortune",    0);

        for (int i = 0; i < totalCalls; i++) {
            String fortune = fortuneService.getRandomFortune();
            resultCountMap.computeIfPresent(fortune, (k, v) -> v + 1);
        }

        double actualGreat  = (double) resultCountMap.get("greatFortune")  / totalCalls;
        double actualMiddle = (double) resultCountMap.get("middleFortune") / totalCalls;
        double actualSmall  = (double) resultCountMap.get("smallFortune")  / totalCalls;
        double actualMis    = (double) resultCountMap.get("misFortune")    / totalCalls;

        double expectedGreat  = fortuneService.getGreat();
        double expectedMiddle = fortuneService.getMiddle();
        double expectedSmall  = fortuneService.getSmall();
        double expectedMis    = fortuneService.getMis();

        double delta = 0.05;

        assertEquals(expectedGreat,  actualGreat,  delta,
            "greatFortune distribution too far off");
        assertEquals(expectedMiddle, actualMiddle, delta,
            "middleFortune distribution too far off");
        assertEquals(expectedSmall,  actualSmall,  delta,
            "smallFortune distribution too far off");
        assertEquals(expectedMis,    actualMis,    delta,
            "misFortune distribution too far off");
    }

    @Test
    @DisplayName("合計確率が0の場合でも例外にならず動作する(テスト実装にあわせて期待動作を調整)")
    void shouldNotFailWhenAllRatesAreZero() {
        fortuneService.setGreat(0.0);
        fortuneService.setMiddle(0.0);
        fortuneService.setSmall(0.0);
        fortuneService.setMis(0.0);

        for (int i = 0; i < 100; i++) {
            String fortune = fortuneService.getRandomFortune();
            assertNotNull(fortune, "Fortune should never be null even if total rates are zero");
        }
    }
}
