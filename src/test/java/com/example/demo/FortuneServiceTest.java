package com.example.demo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

public class FortuneServiceTest {

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
    void testAllFortuneTypesAppear() {
        Set<String> results = new HashSet<>();

        for (int i = 0; i < 1000; i++) {
            results.add(fortuneService.getRandomFortune());
        }

        assertTrue(results.contains("greatFortune"), "should contain greatFortune");
        assertTrue(results.contains("middleFortune"), "should contain middleFortune");
        assertTrue(results.contains("smallFortune"),  "should contain smallFortune");
        assertTrue(results.contains("misFortune"),    "should contain misFortune");
    }

    @Test
    void testRandomDistribution() {
        int total = 100_000;
        int countGreat = 0;
        int countMiddle = 0;
        int countSmall = 0;
        int countMis = 0;

        for (int i = 0; i < total; i++) {
            String fortune = fortuneService.getRandomFortune();
            switch (fortune) {
                case "greatFortune":
                    countGreat++;
                    break;
                case "middleFortune":
                    countMiddle++;
                    break;
                case "smallFortune":
                    countSmall++;
                    break;
                case "misFortune":
                    countMis++;
                    break;
            }
        }

        double actualGreat  = (double) countGreat  / total;
        double actualMiddle = (double) countMiddle / total;
        double actualSmall  = (double) countSmall  / total;
        double actualMis    = (double) countMis    / total;

        double expectedGreat  = fortuneService.getGreat();
        double expectedMiddle = fortuneService.getMiddle();
        double expectedSmall  = fortuneService.getSmall();
        double expectedMis    = fortuneService.getMis();

        assertEquals(expectedGreat,  actualGreat,  0.03, "greatFortune distribution");
        assertEquals(expectedMiddle, actualMiddle, 0.03, "middleFortune distribution");
        assertEquals(expectedSmall,  actualSmall,  0.03, "smallFortune distribution");
        assertEquals(expectedMis,    actualMis,    0.03, "misFortune distribution");
    }
}
