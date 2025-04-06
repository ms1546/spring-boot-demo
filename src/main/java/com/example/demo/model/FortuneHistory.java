package com.example.demo.model;

import java.time.LocalDateTime;

/**
 * 運勢履歴を表すモデルクラス。
 * イミュータブル(フィールド変更不可)にすることでバグを防ぎやすくする。
 */
public class FortuneHistory {

    private final String userName;
    private final String fortune;
    private final LocalDateTime dateTime;

    public FortuneHistory(String userName, String fortune, LocalDateTime dateTime) {
        this.userName = userName;
        this.fortune = fortune;
        this.dateTime = dateTime;
    }

    public String getUserName() {
        return userName;
    }

    public String getFortune() {
        return fortune;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }
}
