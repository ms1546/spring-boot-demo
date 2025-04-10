package com.example.demo.model;

import java.time.LocalDateTime;

/**
 * 運勢履歴を表すレコードクラス
 */
public record FortuneHistory(String userName, String fortune, LocalDateTime dateTime) {

    public FortuneHistory {
        if (userName == null || userName.isBlank()) {
            throw new IllegalArgumentException("User name must not be empty");
        }
        if (fortune == null || fortune.isBlank()) {
            throw new IllegalArgumentException("Fortune must not be empty");
        }
        if (dateTime == null) {
            throw new IllegalArgumentException("DateTime must not be null");
        }
    }
}
