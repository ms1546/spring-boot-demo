package com.example.demo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * fortune.rates.* の値を読み込むための設定クラス。
 */
@Configuration
@ConfigurationProperties(prefix = "fortune.rates")
public class FortuneRateProperties {

    /**
     * 各運勢が出る確率（0.0 ~ 1.0 の範囲を想定）
     */
    private double great;
    private double middle;
    private double small;
    private double mis;

    public double getGreat() {
        return great;
    }
    public void setGreat(double great) {
        this.great = great;
    }

    public double getMiddle() {
        return middle;
    }
    public void setMiddle(double middle) {
        this.middle = middle;
    }

    public double getSmall() {
        return small;
    }
    public void setSmall(double small) {
        this.small = small;
    }

    public double getMis() {
        return mis;
    }
    public void setMis(double mis) {
        this.mis = mis;
    }
}
