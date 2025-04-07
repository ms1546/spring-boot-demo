package com.example.demo.service;

import com.example.demo.config.FortuneRateProperties;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Service
public class FortuneService {

    private final FortuneRateProperties rateProperties;

    public FortuneService(FortuneRateProperties rateProperties) {
        this.rateProperties = rateProperties;
    }

    /**
     * 今日の運勢を取得（キャッシュあり）。
     * ユーザ名ごとに1日1回同じ結果を返したい場合などに利用。
     * @param name ユーザ名
     * @return 今日の運勢
     */
    @Cacheable(value = "todayFortune", key = "#name")
    public String getTodayFortune(String name) {
        return "bad";
    }

    /**
     * 今日の運勢キャッシュを削除。
     * 全ユーザ分のキャッシュをクリアする。
     */
    @CacheEvict(value = "todayFortune", allEntries = true)
    public void clearCache() {
    }

    /**
     * ランダムで運勢を返す。
     * application.yml で設定した各確率に応じて結果が決まる。
     * @return 例: "greatFortune", "middleFortune", "smallFortune", "misFortune"
     */
    public String getRandomFortune() {
        double randomValue = ThreadLocalRandom.current().nextDouble();

        double great = rateProperties.getGreat();
        double middle = rateProperties.getMiddle();
        double small = rateProperties.getSmall();
        double mis = rateProperties.getMis();
        double total = great + middle + small + mis;
        if (total <= 0) {
            return "undefinedFortune";
        }

        double boundaryGreat = great / total;
        double boundaryMiddle = (great + middle) / total;
        double boundarySmall = (great + middle + small) / total;

        if (randomValue < boundaryGreat) {
            return "greatFortune";
        } else if (randomValue < boundaryMiddle) {
            return "middleFortune";
        } else if (randomValue < boundarySmall) {
            return "smallFortune";
        } else {
            return "misFortune";
        }
    }
}
