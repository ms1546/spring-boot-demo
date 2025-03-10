package com.example.demo;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class FortuneService {

    private Map<String, String> fortuneCache = new HashMap<>();

    private final Random random = new Random();

    public String getRandomFortune() {
        double fn = Math.random();
        if (fn >= 0.7) {
            return "greatFortune";
        } else if (fn >= 0.4) {
            return "middleFortune";
        } else if (fn >= 0.1) {
            return "smallFortune";
        } else {
            return "misFortune";
        }
    }

    public String getTodayFortune(String userId) {
        if (userId == null || userId.isEmpty()) {
            userId = "guest";
        }
        String key = userId + "_" + LocalDate.now().toString();

        if (fortuneCache.containsKey(key)) {
            return fortuneCache.get(key);
        }

        String result = getRandomFortune();
        fortuneCache.put(key, result);
        return result;
    }

    public void clearCache() {
        fortuneCache.clear();
    }
}
