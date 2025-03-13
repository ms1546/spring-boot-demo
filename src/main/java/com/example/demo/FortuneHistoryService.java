package com.example.demo;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class FortuneHistoryService {

    private final List<FortuneHistory> historyList = new ArrayList<>();

    public void saveHistory(String userName, String fortune) {
        FortuneHistory history = new FortuneHistory();
        history.setUserName(userName);
        history.setFortune(fortune);
        history.setDateTime(LocalDateTime.now());
        historyList.add(history);
    }

    public List<FortuneHistory> getAllHistory() {
        return new ArrayList<>(historyList);
    }

    public static class FortuneHistory {
        private String userName;
        private String fortune;
        private LocalDateTime dateTime;

        public String getUserName() {
            return userName;
        }
        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getFortune() {
            return fortune;
        }
        public void setFortune(String fortune) {
            this.fortune = fortune;
        }

        public LocalDateTime getDateTime() {
            return dateTime;
        }
        public void setDateTime(LocalDateTime dateTime) {
            this.dateTime = dateTime;
        }
    }
}
