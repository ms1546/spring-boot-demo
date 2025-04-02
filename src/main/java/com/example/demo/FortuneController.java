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
            @RequestParam(name = "name", defaultValue = "") String name,
            Model model
    ) {
        return renderFortuneView(fortuneService.getRandomFortune(), name, model);
    }

    @GetMapping("/today")
    public String showTodayFortune(
            @RequestParam(name = "name", defaultValue = "") String name,
            Model model
    ) {
        return renderFortuneView(fortuneService.getTodayFortune(name), name, model);
    }

    private String renderFortuneView(String fortuneType, String name, Model model) {
        model.addAttribute("name", name);
        return fortuneType + ".html";
    }
}
