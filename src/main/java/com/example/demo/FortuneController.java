package com.example.demo.controller;

import com.example.demo.service.FortuneService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/fortune")
public class FortuneController {

    private final FortuneService fortuneService;

    public FortuneController(FortuneService fortuneService) {
        this.fortuneService = fortuneService;
    }

    @GetMapping
    public String showRandomFortune(
            @RequestParam(name = "name", required = false, defaultValue = "") String name,
            Model model
    ) {
        String fortuneType = fortuneService.getRandomFortune();
        model.addAttribute("name", name);
        return fortuneType + ".html";
    }

    @GetMapping("/today")
    public String showTodayFortune(
            @RequestParam(name = "name", required = false, defaultValue = "") String name,
            Model model
    ) {
        String fortuneType = fortuneService.getTodayFortune(name);
        model.addAttribute("name", name);
        return fortuneType + ".html";
    }
}
