package com.example.demo.service;

import com.example.demo.model.FortuneHistory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 運勢履歴を管理するサービスクラス。
 */
@Service
public class FortuneHistoryService {
    private final List<FortuneHistory> historyList = new CopyOnWriteArrayList<>();

    /**
     * 新しい履歴を保存する。
     *
     * @param userName 保存対象のユーザ名
     * @param fortune  運勢の内容
     */
    public void saveHistory(String userName, String fortune) {
        FortuneHistory history = new FortuneHistory(userName, fortune, LocalDateTime.now());
        historyList.add(history);
    }

    /**
     * 現在までの履歴を取得する。
     * 返却は不変のリストにしておくと安全。
     *
     * @return 履歴のリスト(不変)
     */
    public List<FortuneHistory> getAllHistory() {
        return Collections.unmodifiableList(historyList);
    }
}
