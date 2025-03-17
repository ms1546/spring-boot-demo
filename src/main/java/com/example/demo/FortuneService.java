package com.example.demo;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
@PropertySource("classpath:application.yml")
@ConfigurationProperties(prefix = "fortune.rates")
public class FortuneService {
    private double great;
    private double middle;
    private double small;
    private double mis;

    public double getGreat() { return great; }
    public void setGreat(double great) { this.great = great; }
    public double getMiddle() { return middle; }
    public void setMiddle(double middle) { this.middle = middle; }
    public double getSmall() { return small; }
    public void setSmall(double small) { this.small = small; }
    public double getMis() { return mis; }
    public void setMis(double mis) { this.mis = mis; }

    public String getRandomFortune() {
        double fn = Math.random();
        double sumGreat = great;
        double sumMiddle = great + middle;
        double sumSmall = great + middle + small;

        if (fn <= sumGreat) {
            return "greatFortune";
        } else if (fn <= sumMiddle) {
            return "middleFortune";
        } else if (fn <= sumSmall) {
            return "smallFortune";
        } else {
            return "misFortune";
        }
    }
}
