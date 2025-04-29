package com.example.demo.service;

import com.example.demo.model.FortuneHistory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FortuneStatisticsService {

    private final FortuneHistoryService historyService;

    public FortuneStatisticsService(FortuneHistoryService historyService) {
        this.historyService = historyService;
    }

    public Map<String, Long> getTotalFortuneCounts() {
        return historyService.getAllHistory().stream()
                .collect(Collectors.groupingBy(FortuneHistory::getFortune, Collectors.counting()));
    }

    public Map<String, Long> getUserFortuneCounts(String userName) {
        return historyService.getAllHistory().stream()
                .filter(h -> h.getUserName().equals(userName))
                .collect(Collectors.groupingBy(FortuneHistory::getFortune, Collectors.counting()));
    }

    public Optional<FortuneHistory> getLatestHistory() {
        List<FortuneHistory> list = historyService.getAllHistory();
        return list.isEmpty() ? Optional.empty()
                              : Optional.of(list.get(list.size() - 1));
    }
}
