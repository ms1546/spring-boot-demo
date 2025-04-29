package com.example.demo.controller;

import com.example.demo.service.FortuneService;
import com.example.demo.service.FortuneStatisticsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/fortune")
public class FortuneController {

    private final FortuneService          fortuneService;
    private final FortuneStatisticsService statisticsService;

    public FortuneController(FortuneService fortuneService,
                             FortuneStatisticsService statisticsService) {
        this.fortuneService    = fortuneService;
        this.statisticsService = statisticsService;
    }

    @GetMapping
    public String showRandomFortunePage(
            @RequestParam(name = "name", defaultValue = "") String name,
            Model model
    ) {
        return buildFortuneView(fortuneService.getRandomFortune(), name, model);
    }

    @GetMapping("/today")
    public String showTodayFortunePage(
            @RequestParam(name = "name", defaultValue = "") String name,
            Model model
    ) {
        return buildFortuneView(fortuneService.getTodayFortune(name), name, model);
    }

    @GetMapping("/stats")
    public String showTotalStats(Model model) {
        model.addAttribute("title", "全体の運勢統計");
        model.addAttribute("stats", statisticsService.getTotalFortuneCounts());
        model.addAttribute("latest", statisticsService.getLatestHistory().orElse(null));
        return "stats.html";
    }

    @GetMapping(value = "/stats", params = "name")
    public String showUserStats(
            @RequestParam String name,
            Model model
    ) {
        model.addAttribute("title", name + " さんの運勢統計");
        model.addAttribute("stats", statisticsService.getUserFortuneCounts(name));
        model.addAttribute("latest", statisticsService.getLatestHistory().orElse(null));
        model.addAttribute("name", name);
        return "stats.html";
    }

    private String buildFortuneView(String fortuneType, String name, Model model) {
        model.addAttribute("name", name);
        model.addAttribute("fortuneType", fortuneType);
        return fortuneType + ".html";
    }
}
